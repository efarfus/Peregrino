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
import com.angellira.reservafrotas.preferences.Preferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OcorrenciasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcorrenciasBinding
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        recyclerView(prefs.idCarroSelected.toString())
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.textData.text = currentDate.toString()

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }

        binding.buttonRegistrar.setOnClickListener {
            val editProfileDialog = EditProfileDialogOcorrenciasFragment()

            // Exiba o DialogFragment
            editProfileDialog.show(supportFragmentManager, "EditProfileDialogOcorrencias")
        }
    }

    private fun recyclerView(carId: String?) {
        val recyclerView = binding.recyclerViewOcorrencias

        // Simulação de ocorrências do banco de dados
        val allOccurrences = listOf(
            Ocorrencias("Troca de óleo", -337.52, isPositive = false, carId = "8974d275-a151-404f-8539-ff3a7677b966", date = "2024-11-24"),
            Ocorrencias("Gorjeta", 351.40, isPositive = true, carId = "8974d275-a151-404f-8539-ff3a7677b966", date = "2024-11-24"),
            Ocorrencias("Lavagem", -50.00, isPositive = false, carId = "8974d275-a151-404f-8539-ff3a7677b966", date = "2024-11-22"),
            Ocorrencias("Pneu furado", -200.00, isPositive = false, carId = "1", date = "2024-11-21")
        )

        // Filtrar por carro e data atual
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val filteredOccurrences = allOccurrences.filter {
            it.carId == carId && it.date == currentDate
        }

        // Configurar o RecyclerView
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.removeItemDecoration(itemDecoration)

        val adapter = OcorrenciasAdapter(filteredOccurrences)
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