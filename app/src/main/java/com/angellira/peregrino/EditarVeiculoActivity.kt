package com.angellira.peregrino

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityEditarVeiculoBinding
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRelatoriosBinding

class EditarVeiculoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarVeiculoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityEditarVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}