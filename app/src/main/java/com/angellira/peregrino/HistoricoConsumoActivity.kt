package com.angellira.peregrino

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.adapter.HistoricoConsumoAdapter
import com.angellira.peregrino.databinding.ActivityHistoricoConsumoBinding
import com.angellira.peregrino.model.HistoricoConsumo

class HistoricoConsumoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricoConsumoBinding
    private lateinit var adapter: HistoricoConsumoAdapter
    private lateinit var historicoList: MutableList<HistoricoConsumo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        recyclerView()
    }

    private fun recyclerView() {
        binding.recyclerViewHistorico.layoutManager = LinearLayoutManager(this)

        historicoList = mutableListOf(
            HistoricoConsumo("001", "10/11/2022", "10"),
            HistoricoConsumo("002", "21/08/2024", "7")
        )

        adapter = HistoricoConsumoAdapter(historicoList)
        binding.recyclerViewHistorico.adapter = adapter
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityHistoricoConsumoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}