package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityConsumoCombustivelBinding
import com.angellira.peregrino.databinding.ActivityLoginBinding
import com.angellira.reservafrotas.preferences.Preferences

class ConsumoCombustivelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsumoCombustivelBinding
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.inputMedia.text = prefs.mediaFinal //vai ter que ser alterado para a requisição posteriormente

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonCalcular.setOnClickListener {
            startActivity(Intent(this, calculoPasso1::class.java))
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }

        binding.buttonHistorico.setOnClickListener {
            startActivity(Intent(this, HistoricoConsumoActivity::class.java))
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityConsumoCombustivelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}