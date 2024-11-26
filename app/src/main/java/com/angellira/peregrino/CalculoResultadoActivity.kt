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
import com.angellira.peregrino.model.HistoricoConsumo
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
    private val historico = HistoricoConsumo()

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
        historico.data = LocalDate.now().toString()
        historico.id = prefs.idCarroSelected.toString()
        historico.eficiencia = eficienciaFormatada.toString()
        binding.buttonRegistrar.setOnClickListener {
            lifecycleScope.launch(IO) {
                updateVehicleMediaById(prefs.idCarroSelected.toString(), eficienciaFormatada)
                serviceApi.postHistoricoConsumo(historico)

            }
            startActivity(Intent(this, ConsumoCombustivelActivity::class.java))
            finishAffinity()
        }
    }
    suspend fun updateVehicleMediaById(vehicleId: String, newMedia: String) {
        // Passo 1: Recupera todos os veículos do Firebase
        val vehiclesMap = serviceApi.getCars() // Retorna um Map<String, Veiculo>

        // Passo 2: Encontra o nó cujo campo `id` corresponde ao `vehicleId`
        val vehicleNode = vehiclesMap.entries.find { it.value.id == vehicleId }

        if (vehicleNode != null) {
            val nodeId = vehicleNode.key // ID do nó gerado pelo Firebase
            val updatedFields = mapOf("mediaVeiculo" to newMedia) // Apenas o campo a ser atualizado

            // Passo 3: Atualiza o campo `mediaVeiculo` do veículo no Firebase
            serviceApi.updateVehicle(nodeId, updatedFields)
            println("Veículo com ID $vehicleId atualizado com sucesso!")
        } else {
            println("Veículo com ID $vehicleId não encontrado!")
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