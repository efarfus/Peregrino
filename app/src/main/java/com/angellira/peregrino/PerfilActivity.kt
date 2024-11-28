package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
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
            val users = serviceApi.getUsers().values.find { it.id == prefs.id }
            users?.let {
                binding.nameInput.setText(it.name)
                binding.emailInput.setText(it.email)
                binding.cpfInput.setText(it.cpf)
                binding.senhaInput.setText(it.senha)
            } ?: run {
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