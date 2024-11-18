package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.angellira.peregrino.adapter.OcorrenciasAdapter
import com.angellira.peregrino.databinding.ActivityOcorrenciasBinding
import com.angellira.peregrino.model.Ocorrencias

class OcorrenciasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcorrenciasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        recyclerView()

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }
    }

    private fun recyclerView() {
        val recyclerView = binding.recyclerViewOcorrencias
        val transactions = listOf(
            Ocorrencias("Troca de óleo", -337.52, isPositive = false),
            Ocorrencias("Gorjeta", 351.40, isPositive = true)
        )


        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.removeItemDecoration(itemDecoration)

        val adapter = OcorrenciasAdapter(transactions)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityOcorrenciasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}