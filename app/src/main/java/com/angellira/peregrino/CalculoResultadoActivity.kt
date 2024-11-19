package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityCalculoPasso3Binding
import com.angellira.peregrino.databinding.ActivityCalculoPasso4Binding
import com.angellira.peregrino.databinding.ActivityCalculoResultadoBinding

class CalculoResultadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculoResultadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, ConsumoCombustivelActivity::class.java))
            finishAffinity()
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCalculoResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}