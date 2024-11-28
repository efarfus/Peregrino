package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityCalendarioBinding
import com.angellira.peregrino.network.ApiServicePeregrino
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding
    private var selectedDate: String = ""

    private val corridasApi = ApiServicePeregrino.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        logicaCalendario()

        binding.buttonVolta.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun logicaCalendario() {
        val calendarView = binding.calendarView
        val dateInfo = binding.informacaoData
        selectedDate = formatarData(calendarView.date.toString())

        pegandoEmostrandoData(selectedDate, dateInfo)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
            pegandoEmostrandoData(selectedDate, dateInfo)
        }
    }

    fun formatarData(data: String): String {
        return try {
            val timestamp = data.toLong()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = Date(timestamp)
            sdf.format(date)
        } catch (e: NumberFormatException) {
            val sdfEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dataFormatada = sdfEntrada.parse(data)
            val sdfSaida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdfSaida.format(dataFormatada)
        }
    }



    private fun pegandoEmostrandoData(date: String, dateInfo: TextView) {
        lifecycleScope.launch {
            try {
                val corridas = corridasApi.obterCorridas()
                val corridasNoDia = corridas.values.filter { corrida ->
                    val corridaDataFormatada = formatarData(corrida.data)
                    corridaDataFormatada == date
                }

                dateInfo.text = if (corridasNoDia.isNotEmpty()) {
                    "Corridas para $date:\n${corridasNoDia.joinToString("\n") { " -> Custo: ${it.custo}, Ponto Inicial: ${it.pontoInicial}, Ponto Final: ${it.pontoFinal}\n " }}"
                } else {
                    "Nenhuma corrida registrada para $date."
                }
            } catch (e: Exception) {
                Toast.makeText(this@CalendarioActivity, "Erro ao buscar dados de corridas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
