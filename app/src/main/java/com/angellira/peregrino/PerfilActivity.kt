package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityPerfilBinding
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.launch

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    private val serviceApi = ApiServicePeregrino.retrofitService
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        binding.buttonVolta.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            try {
                val usersMap = serviceApi.getUsers()
                Log.d("Perfil", "Usuários carregados: $usersMap")

                val user = usersMap[prefs.id]
                if (user != null) {
                    Log.d("Perfil", "Usuário encontrado: ${user.name}")
                    binding.nameInput.setText(user.name)
                    binding.emailInput.setText(user.email)
                    binding.cpfInput.setText(user.cpf)
                    binding.senhaInput.setText(user.senha)
                } else {
                    Log.d("Perfil", "Usuário não encontrado com o ID: ${prefs.id}")
                }
            } catch (e: Exception) {
                Log.e("Perfil", "Erro ao buscar usuário: $e")
            }
        }




        binding.buttonLogout.setOnClickListener {
            prefs.isLogged = false
            startActivity(Intent(this, SplashActivity::class.java))
            finishAffinity()
        }


    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}