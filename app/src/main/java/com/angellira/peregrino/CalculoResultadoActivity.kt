package com.angellira.peregrino

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityCalculoResultadoBinding
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDate

class CalculoResultadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculoResultadoBinding
    private val prefs by lazy { Preferences(this) }
    private val serviceApi = ApiServicePeregrino.retrofitService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        val litrosConsumidos = prefs.totalLitros!!.toFloat() - prefs.litrosRestantes!!.toFloat()

        val kmPorLitro = 80.0 / litrosConsumidos
        val df = DecimalFormat("#.#") // Formata para uma casa decimal
        val eficienciaFormatada = df.format(kmPorLitro)

        // Salva a média calculada nas preferências
        prefs.mediaFinal = eficienciaFormatada
        binding.textView2.text = "${prefs.mediaFinal} km por litro"


        binding.buttonRegistrar.setOnClickListener {
            lifecycleScope.launch(IO) {
                updateVehicleMediaById(prefs.idCarroSelected.toString(), prefs.mediaFinal.toString(), LocalDate.now().toString())
            }
            startActivity(Intent(this, ConsumoCombustivelActivity::class.java))
            finishAffinity()
        }
    }

    suspend fun updateVehicleMediaById(vehicleId: String, newMedia: String, newDate: String) {
        // Recupera todos os veículos
        val veiculos = serviceApi.getCars() // Essa função deve recuperar um Map<String, Veiculo>

        // Encontra o veículo que corresponde ao campo `id`
        val vehicleToUpdate = veiculos.entries.find { it.value.id == vehicleId } // A busca é feita dentro dos valores

        if (vehicleToUpdate != null) {
            val key = vehicleToUpdate.key
            val updatedVehicle = vehicleToUpdate.value.copy(mediaVeiculo = newMedia)

            serviceApi.updateVehicle(key, updatedVehicle, newDate)
            println("Veículo atualizado com sucesso!")
        } else {
            println("Veículo com o ID $vehicleId não encontrado!")
        }
    }






    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityCalculoResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}