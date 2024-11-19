package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityCalculoPasso2Binding
import com.angellira.peregrino.databinding.ActivityCalculoPasso3Binding

class CalculoPasso3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculoPasso3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, CalculoPasso4Activity::class.java))
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCalculoPasso3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}