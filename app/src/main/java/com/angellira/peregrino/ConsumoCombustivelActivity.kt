package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityConsumoCombustivelBinding
import com.angellira.peregrino.databinding.ActivityLoginBinding
import com.angellira.peregrino.model.Veiculo
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConsumoCombustivelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsumoCombustivelBinding
    private val prefs by lazy { Preferences(this) }
    private val serviceApi = ApiServicePeregrino.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()


        lifecycleScope.launch(IO){
            val carro = getCarByInternalId(prefs.idCarroSelected.toString())

            withContext(Main){
                binding.textModeloCarro.text = carro!!.modelo
                binding.textApelidoCarro.text = carro.apelido
                binding.inputMedia.text = carro.mediaVeiculo
            }

        }

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonCalcular.setOnClickListener {
            startActivity(Intent(this, calculoPasso1::class.java))
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }

        binding.buttonHistorico.setOnClickListener {
            startActivity(Intent(this, HistoricoConsumoActivity::class.java))
        }
    }

    suspend fun getCarByInternalId(id: String): Veiculo? {
        val response = serviceApi.getCars()
        return response.values.find { it.id == id }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityConsumoCombustivelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}