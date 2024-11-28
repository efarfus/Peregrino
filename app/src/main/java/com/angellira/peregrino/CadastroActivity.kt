package com.angellira.peregrino

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityCadastroBinding
import com.angellira.peregrino.model.User
import com.angellira.peregrino.network.ApiServicePeregrino
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val corridasApi = ApiServicePeregrino.retrofitService
    private val user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        maskCpf()
        clickSingInButton()

        binding.buttonVolta.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    private fun clickSingInButton() {
        binding.button.setOnClickListener {
            validateFields()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun validateFields(
    ): Boolean {
        if (binding.nameInput.text.toString()
                .isBlank() || binding.nameInput.text.toString().length < 6
        ) {
            binding.errorName.visibility = VISIBLE
            binding.errorName.text = "Por favor, insira um nome com no mínimo 6 caractéres"
            binding.scrollView.smoothScrollTo(0, binding.nameInput.top)
            return false
        }

        binding.errorName.visibility = GONE

        if (binding.cpfInput.text.toString()
                .isEmpty() || binding.cpfInput.text.toString().length != 14
        ) {
            binding.errorCpf.visibility = VISIBLE
            binding.errorCpf.text = "Por favor, insira um CPF válido"
            binding.scrollView.smoothScrollTo(0, binding.cpfInput.top)
            return false
        }

        binding.errorCpf.visibility = GONE


        if (binding.emailInput.text.toString()
                .isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.text.toString())
                .matches()
        ) {
            binding.errorEmail.visibility = VISIBLE
            binding.errorEmail.text = "Por favor, insira um e-mail válido"
            binding.scrollView.smoothScrollTo(0, binding.emailInput.top)
            return false
        }

        binding.errorEmail.visibility = GONE

        if (binding.passwordInput.text.toString()
                .isEmpty() || binding.passwordInput.text.toString().length < 9 || !isValidPassword(
                binding.passwordInput.text.toString()
            )
        ) {
            binding.errorPassword.visibility = VISIBLE
            binding.errorPassword.text =
                "A senha deve ter pelo menos 9 caracteres, contendo letra, número e um caracter especial"
            binding.scrollView.smoothScrollTo(0, binding.passwordInput.top)
            return false
        }

        binding.errorPassword.visibility = GONE
        if (binding.passwordConfirmationInput.text.toString()
                .isEmpty() || binding.passwordConfirmationInput.text.toString() != binding.passwordInput.text.toString()
        ) {
            binding.errorConfirmPassword.visibility = VISIBLE
            binding.scrollView.smoothScrollTo(0, binding.passwordConfirmationInput.top)
            binding.errorConfirmPassword.text = "As senhas devem coincidir"
            return false
        }

        binding.errorConfirmPassword.visibility = GONE

        post()
        return true
    }

    private fun post() {
        lifecycleScope.launch(IO) {
            try {
                val users = corridasApi.getUsers().values.toList()
                val existingUser = users.find { it.cpf == binding.cpfInput.text.toString() }

                if (existingUser != null) {
                    withContext(Main) {
                        Toast.makeText(
                            this@CadastroActivity,
                            "E-mail já cadastrado, tente usar outro.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    user.id = UUID.randomUUID().toString()
                    user.name = binding.nameInput.text.toString()
                    user.email = binding.emailInput.text.toString()
                    user.senha = binding.passwordInput.text.toString()
                    user.cpf = binding.cpfInput.text.toString()

                    val response = corridasApi.registrarUsuario(user)

                    if (response.isSuccessful) {
                        withContext(Main) {
                            Toast.makeText(
                                this@CadastroActivity,
                                "Usuário cadastrado com sucesso",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@CadastroActivity, LoginActivity::class.java))
                            finishAffinity()
                        }
                    } else {
                        withContext(Main) {
                            Toast.makeText(
                                this@CadastroActivity,
                                "Erro ao cadastrar usuário: ${response.errorBody()?.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Erro ao cadastrar usuário: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("Cadastro", "Erro: $e")
                }
            }
        }
    }


    private fun maskCpf() {
        val cpfEditText = binding.cpfInput

        cpfEditText.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return
                isEditing = true

                val cpf =
                    s.toString().replace("[^\\d]".toRegex(), "") // Remove tudo que não é número

                val formattedCpf = when {
                    cpf.length <= 3 -> cpf
                    cpf.length <= 6 -> "${cpf.substring(0, 3)}.${cpf.substring(3)}"
                    cpf.length <= 9 -> "${cpf.substring(0, 3)}.${
                        cpf.substring(
                            3, 6
                        )
                    }.${cpf.substring(6)}"

                    cpf.length <= 11 -> "${cpf.substring(0, 3)}.${
                        cpf.substring(
                            3, 6
                        )
                    }.${cpf.substring(6, 9)}-${cpf.substring(9)}"

                    else -> cpf
                }

                cpfEditText.setText(formattedCpf)
                cpfEditText.setSelection(formattedCpf.length) // Manter o cursor no final
                isEditing = false
            }
        })
    }

    private fun isValidPassword(password: String): Boolean {
        val regex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#^+=_()-])[A-Za-z\\d@$!%*?&#^+=_()-]{9,}$")
        return regex.matches(password)
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        topBar()
    }

    private fun topBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}