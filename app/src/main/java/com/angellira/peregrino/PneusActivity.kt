package com.angellira.peregrino

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityPneusBinding
import com.angellira.peregrino.model.Pneu
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.launch

class PneusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPneusBinding
    private val prefs by lazy { Preferences(this) }
    private val serviceApi = ApiServicePeregrino.retrofitService

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }

        binding.buttonRegistrar.setOnClickListener {
            val editProfileDialog = EditProfileDialogFragment()

            // Exiba o DialogFragment
            editProfileDialog.show(supportFragmentManager, "EditProfileDialog")
        }

        binding.picCarro.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x
                val y = event.y
                val width = v.width
                val height = v.height

                when {
                    x < width / 2 && y < height / 2 -> {
                        handleTireClick("Dianteiro Esquerdo")
                    }

                    x >= width / 2 && y < height / 2 -> {
                        handleTireClick("Dianteiro Direito")
                    }

                    x < width / 2 && y >= height / 2 -> {
                        handleTireClick("Traseiro Esquerdo")
                    }

                    x >= width / 2 && y >= height / 2 -> {
                        handleTireClick("Traseiro Direito")
                    }
                }

                v.performClick() // Acessibilidade
            }
            true
        }

    }


    private suspend fun getPneusByCarIdAndPosition(carId: String, position: String): Pneu? {
        return try {
            serviceApi.getPneus().values
                .filter { it.carId == carId && it.posicao == position }
                .maxByOrNull { it.timestamp } // Obtém o pneu com o maior timestamp
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityPneusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleTireClick(tirePosition: String) {
        lifecycleScope.launch {
            val carId = prefs.idCarroSelected.toString() // Obtém o ID do carro selecionado
            val pneu = getPneusByCarIdAndPosition(carId, tirePosition)
            val carroSelecionado = serviceApi.getCars().values.find { it.id == carId }
            if (pneu != null) {
                // Atualiza as informações exibidas na tela com os dados do pneu clicado
                binding.idPneu.text = "ID: ${pneu.id}"
                binding.fabricante.text = "Fabricante: ${pneu.fabricante}"
                binding.aro.text = "Aro: ${pneu.aro}"
                binding.ultimoEnchimento.text = "Último enchimento: ${pneu.ultimoEnchimento}"
                binding.ultimaTroca.text = "Última troca do pneu: ${pneu.ultimaTroca}"
                binding.qualPneu.text =
                    "Pneu do carro: ${carroSelecionado!!.apelido}, ${pneu.posicao}"
            } else {
                // Caso não seja encontrado um pneu para a posição clicada
                Toast.makeText(
                    this@PneusActivity,
                    "Nenhum pneu encontrado para a posição: $tirePosition e carro ID: $carId",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}
