package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRegistrarCorridasBinding
import com.angellira.peregrino.databinding.ActivityRegistroDeCorridaBinding

class RegistroDeCorridaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroDeCorridaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        binding.buttonVolta.setOnClickListener{
            startActivity(Intent(this@RegistroDeCorridaActivity, MainActivity::class.java))
        }
        binding.buttonCorridas.setOnClickListener{
            startActivity(Intent(this@RegistroDeCorridaActivity, RegistrarCorridas::class.java))
        }
        binding.buttonRelatorios.setOnClickListener{
            startActivity(Intent(this@RegistroDeCorridaActivity, RelatoriosActivity::class.java))
        }
    }



    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityRegistroDeCorridaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}