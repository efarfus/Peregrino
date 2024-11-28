package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                    totalPositivo += cleanCurrencyValue(ocorrencia.value)
                }

                var totalNegativo = 0.0
                for (ocorrencia in ocorrenciasnegativas) {
                    totalNegativo += cleanCurrencyValue(ocorrencia.value)
                }

                var totalCorridas = 0.0
                for (corrida in corridas) {
                    totalCorridas += cleanCurrencyValue(corrida.value.custo.toString())
                }

                val totalGeral = totalPositivo - totalNegativo + totalCorridas

                binding.valorGastos.text = ("R$ ${totalNegativo}")
                binding.valorPositivo.text = ("R$ ${totalPositivo}")
                binding.geral.text = ("R$ ${totalGeral}")

            } catch (e: Exception) {
                Log.d("Mostrar", "${e.message}")
                Toast.makeText(this@RelatoriosActivity, "Erro ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para limpar e converter a string de moeda para número
    private fun cleanCurrencyValue(value: String): Double {
        // Remove o símbolo "R$", espaços e substitui a vírgula por ponto
        val cleanedValue = value.replace("R$", "")
            .replace("Â", "") // Remove qualquer caractere extra "Â"
            .replace("\\s".toRegex(), "") // Remove espaços em branco
            .replace(",", ".") // Substitui a vírgula por ponto para conversão correta

        return try {
            cleanedValue.toDouble() // Tenta converter a string para Double
        } catch (e: NumberFormatException) {
            0.0 // Retorna 0.0 em caso de erro
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