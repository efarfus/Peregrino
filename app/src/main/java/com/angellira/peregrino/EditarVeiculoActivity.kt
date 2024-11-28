package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityEditarVeiculoBinding
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRelatoriosBinding
import com.angellira.peregrino.model.Veiculo
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarVeiculoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarVeiculoBinding
    private val prefs by lazy { Preferences(this) }
    private val serviceApi = ApiServicePeregrino.retrofitService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonPneus.setOnClickListener {
            startActivity(Intent(this, PneusActivity::class.java))
        }

        binding.buttonConsumo.setOnClickListener {
            startActivity(Intent(this, ConsumoCombustivelActivity::class.java))
        }

        binding.buttonOcorrencias.setOnClickListener {
            startActivity(Intent(this, OcorrenciasActivity::class.java))
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }



        prefs.idCarroSelected = intent.getStringExtra("CAR_ID")

        suspend fun getCarByInternalId(id: String): Veiculo? {
            val response = serviceApi.getCars()
            return response.values.find { it.id == id }
        }

        lifecycleScope.launch(IO){
            val carro = getCarByInternalId(prefs.idCarroSelected.toString())

            withContext(Main){
                binding.textModeloCarro.text = carro!!.modelo
                binding.textApelidoCarro.text = carro.apelido
            }

        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityEditarVeiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}