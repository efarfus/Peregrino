package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
        binding.buttonRelatorios.setOnClickListener {
            startActivity(Intent(this, RelatoriosActivity::class.java))
        }
        binding.buttonVeiculos.setOnClickListener {
            startActivity(Intent(this, VeiculosActivity::class.java))
        }
        binding.buttonCorridas.setOnClickListener{
            startActivity(Intent(this@MainActivity, RegistroDeCorridaActivity::class.java))
        }
        binding.buttonCalendario.setOnClickListener{
            startActivity(Intent(this@MainActivity, CalendarioActivity::class.java))
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}