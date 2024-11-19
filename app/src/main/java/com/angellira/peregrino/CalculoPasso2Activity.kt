package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityCalculoPasso1Binding
import com.angellira.peregrino.databinding.ActivityCalculoPasso2Binding

class CalculoPasso2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculoPasso2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonRegistrar.setOnClickListener {
            startActivity(Intent(this, CalculoPasso3Activity::class.java))
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCalculoPasso2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}