package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityRegistrarCorridasBinding
import com.angellira.peregrino.databinding.ActivityRelatoriosBinding
import com.angellira.peregrino.network.ApiServicePeregrino
import kotlinx.coroutines.launch

class RelatoriosActivity : AppCompatActivity() {

    private val corridasApi = ApiServicePeregrino.retrofitService
    private lateinit var binding: ActivityRelatoriosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        binding.buttonVolta.setOnClickListener{
            startActivity(Intent(this, RegistroDeCorridaActivity::class.java))
        }
        relatorioGastos()
    }

    private fun relatorioGastos() {
        lifecycleScope.launch {
            try {
                val corridas = corridasApi.obterCorridas()
                val ocorrencias = corridasApi.getOcorrencias()

                val ocorrenciaspositivas = ocorrencias.values.filter { it.isPositive }
                val ocorrenciasnegativas = ocorrencias.values.filter { it.isPositive == false }

                var totalPositivo = 0.0
                for (ocorrencia in ocorrenciaspositivas) {
                    totalPositivo += ocorrencia.value.toDouble()
                }

                var totalNegativo = 0.0
                for (ocorrencia in ocorrenciasnegativas) {
                    totalNegativo += ocorrencia.value.toDouble()
                }

                var totalCorridas = 0.0
                for (corridas in corridas) {
                    totalCorridas += corridas.value.custo.toDouble()
                }

                val totalGeral = totalPositivo - totalNegativo + totalCorridas


                binding.valorGastos.setText(totalNegativo.toString())
                binding.valorPositivo.setText(totalPositivo.toString())
                binding.geral.setText(totalGeral.toString())

            } catch (e: Exception) {
                Toast.makeText(this@RelatoriosActivity, "Erro", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityRelatoriosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}