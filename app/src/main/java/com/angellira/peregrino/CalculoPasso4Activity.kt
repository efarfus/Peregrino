package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityCalculoPasso3Binding
import com.angellira.peregrino.databinding.ActivityCalculoPasso4Binding
import com.angellira.reservafrotas.preferences.Preferences

class CalculoPasso4Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculoPasso4Binding
    private val prefs by lazy { Preferences(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonRegistrar.setOnClickListener {
            val inputText = binding.nameInput.text?.toString()

            if (inputText.isNullOrBlank()) {
                binding.nameText.error = "Este campo é obrigatório"
            } else {
                binding.nameText.error = null
                prefs.litrosRestantes = inputText
                startActivity(Intent(this, CalculoResultadoActivity::class.java))
            }
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCalculoPasso4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}