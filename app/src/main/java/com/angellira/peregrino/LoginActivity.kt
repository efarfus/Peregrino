package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
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
            startActivity(main)
            logar(binding.buttonEntrar, binding.labelTextCPF, binding.labelTextPass, main)
        }
    }

    private suspend fun checkCredentials(email: String, senha: String): Boolean {
        return if (email.isNotEmpty() && senha.isNotEmpty()) {
            try {
                users = apiService.getUsers().toMutableList()
                return users.any { it.email == email && it.senha == senha }
            } catch (e: Exception) {
                Log.e("errado", e.toString())
                return false
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
            Log.e("errado", e.toString())
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