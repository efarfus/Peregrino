package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRegistrarCorridasBinding

class RegistrarCorridas : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarCorridasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        binding.buttonVolta.setOnClickListener{
            startActivity(Intent(this, RegistroDeCorridaActivity::class.java))
        }
    }



    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityRegistrarCorridasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}