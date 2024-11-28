package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityCalendarioBinding
import com.angellira.peregrino.databinding.ActivityRegistroDeCorridaBinding
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarioBinding
    private val notesMap = mutableMapOf<String, String>()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        logicaCalendario()
        binding.buttonVolta.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun logicaCalendario() {
        val calendarView = binding.calendarView
        val dateInfo = binding.informacaoData
        selectedDate = formatarData(calendarView.date)

        pegandoEmostrandoData(selectedDate, dateInfo)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
            pegandoEmostrandoData(selectedDate, dateInfo)
        }
    }

    private fun formatarData(timeInMillis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(timeInMillis)
    }

    private fun pegandoEmostrandoData(date: String, dateInfo: TextView) {
        val externalData = pegandoDadosData(date)

        dateInfo.text = if (externalData.isNotEmpty()) {
            "Registros para $date:\n${externalData.joinToString("\n")}"
        } else {
            "Nenhum registro encontrado para $date."
        }
    }

    private fun pegandoDadosData(date: String): List<String> {
        val mockData = mapOf(
            "21/11/2024" to listOf("Consulta médica", "Vacina do pet"),
            "22/11/2024" to listOf("Reunião às 10h", "Entrega de relatório"),
            "23/11/2024" to listOf("Aniversário da Ana")
        )
        return mockData[date] ?: emptyList()
    }

    private fun setupView(){
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