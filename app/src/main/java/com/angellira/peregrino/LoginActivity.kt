package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityLoginBinding
import com.angellira.peregrino.model.User
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService = ApiServicePeregrino.retrofitService
    private val prefs by lazy { Preferences(this) }
    private var users: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        val main = Intent(this, MainActivity::class.java)
        val cadastro = Intent(this, CadastroActivity::class.java)

        binding.buttonCadastrar.setOnClickListener {
            startActivity(cadastro)
        }

        binding.buttonEntrar.setOnClickListener {
            logar(binding.buttonEntrar, binding.labelTextCPF, binding.labelTextPass, main)
        }

        binding.labelTextCPF.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Não é necessário implementar este método para a máscara
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Remove o TextWatcher temporariamente para evitar loop infinito
                binding.labelTextCPF.removeTextChangedListener(this)

                val text = charSequence.toString().replace("[^\\d]".toRegex(), "") // Remove tudo que não é número

                if (text.length <= 11) {
                    // Adiciona a máscara de cpf
                    val masked = text.replaceFirst(
                        "(\\d{3})(\\d{3})(\\d{3})(\\d{2})".toRegex(),
                        "$1.$2.$3-$4"
                    )
                    binding.labelTextCPF.setText(masked)
                    binding.labelTextCPF.setSelection(masked.length) // Coloca o cursor no final
                }

                // Re-adiciona o TextWatcher
                binding.labelTextCPF.addTextChangedListener(this)
            }

            override fun afterTextChanged(editable: Editable?) {
                // Não é necessário implementar este método para a máscara
            }
        })
    }

    private suspend fun checkCredentials(cpf: String, senha: String): Boolean {
        return if (cpf.isNotEmpty() && senha.isNotEmpty()) {
            try {
                users = apiService.getUsers().values.toMutableList()
                users.any { it.cpf == cpf && it.senha == senha }
            } catch (e: Exception) {
                Log.e("LoginError", "Erro ao verificar as credenciais: ${e.message}")
                false
            }
        } else {
            false
        }
    }

    private fun clear(caixaEmail: EditText, caixaSenha: EditText) {
        caixaEmail.text.clear()
        caixaSenha.text.clear()
    }

    private fun logar(
        envioEmailSenha: Button,
        caixaEmail: EditText,
        caixaSenha: EditText,
        pagMain: Intent
    ) {
        envioEmailSenha.setOnClickListener {
            val emailTentado = caixaEmail.text.toString()
            val senhaTentada = caixaSenha.text.toString()

            lifecycleScope.launch(IO) {
                if (checkCredentials(emailTentado, senhaTentada)) {
                    // Espera o saveId ser concluído antes de continuar
                    saveId(emailTentado, senhaTentada)
                    withContext(Main) {
                        prefs.isLogged = true
                        startActivity(pagMain)
                        clear(caixaEmail, caixaSenha)
                    }
                } else {
                    withContext(Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email ou senha incorretos",
                            Toast.LENGTH_LONG
                        ).show()
                        clear(caixaSenha, caixaEmail)
                    }
                }
            }
        }
    }

    private suspend fun saveId(email: String, password: String) {
        try {
            val userId = apiService.getUserByEmailAndPassword(email, password).id
            prefs.id = userId
        } catch (e: Exception) {
            Log.e("SaveIdError", "Erro ao salvar ID: ${e.message}")
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
