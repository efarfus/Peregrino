package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.angellira.peregrino.adapter.OcorrenciasAdapter
import com.angellira.peregrino.databinding.ActivityOcorrenciasBinding
import com.angellira.peregrino.model.Ocorrencias
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OcorrenciasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOcorrenciasBinding
    private val prefs by lazy { Preferences(this) }
    private lateinit var ocorrenciasAdapter: OcorrenciasAdapter
    private val serviceApi = ApiServicePeregrino.retrofitService

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
            val dialogFragment = EditProfileDialogOcorrenciasFragment()

            // Registra o callback que será chamado quando o formulário for salvo
            dialogFragment.setOnSaveListener {
                reloadRecyclerView()  // Atualiza o RecyclerView ou qualquer outra ação necessária
            }

            // Exibe o DialogFragment
            dialogFragment.show(supportFragmentManager, "EditProfileDialogOcorrencias")
        }
    }

    private fun reloadRecyclerView() {
        lifecycleScope.launch(IO) {
            val allOccurrences = serviceApi.getOcorrencias().values.toList()
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val updatedList = allOccurrences.filter {
                it.carId == prefs.idCarroSelected.toString() && it.date == currentDate
            }

            withContext(Main) {
                ocorrenciasAdapter.updateData(updatedList)
            }
        }
    }





    private fun recyclerView(carId: String?) {
        val recyclerView = binding.recyclerViewOcorrencias

        lifecycleScope.launch(IO) {
            val allOccurrences = serviceApi.getOcorrencias().values.toList()
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val filteredOccurrences = allOccurrences.filter {
                it.carId == carId && it.date == currentDate
            }

            withContext(Main) {
                val itemDecoration = DividerItemDecoration(this@OcorrenciasActivity, DividerItemDecoration.VERTICAL)
                recyclerView.removeItemDecoration(itemDecoration)

                // Inicialize o adapter com uma lista mutável
                ocorrenciasAdapter = OcorrenciasAdapter(filteredOccurrences.toMutableList())
                recyclerView.adapter = ocorrenciasAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@OcorrenciasActivity)
            }
        }
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