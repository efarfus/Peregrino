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
import androidx.recyclerview.widget.LinearLayoutManager
import com.angellira.peregrino.databinding.ActivityBottomSheetBinding
import com.angellira.peregrino.databinding.ActivityPneusBinding
import com.angellira.peregrino.model.Pneu
import com.angellira.reservafrotas.preferences.Preferences

class PneusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPneusBinding
    private val prefs by lazy { Preferences(this) }


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
                        // Quadrante superior esquerdo (pneu dianteiro esquerdo)
                        handleTireClick("Dianteiro Esquerdo")
                    }
                    x >= width / 2 && y < height / 2 -> {
                        // Quadrante superior direito (pneu dianteiro direito)
                        handleTireClick("Dianteiro Direito")
                    }
                    x < width / 2 && y >= height / 2 -> {
                        // Quadrante inferior esquerdo (pneu traseiro esquerdo)
                        handleTireClick("Traseiro Esquerdo")
                    }
                    x >= width / 2 && y >= height / 2 -> {
                        // Quadrante inferior direito (pneu traseiro direito)
                        handleTireClick("Traseiro Direito")
                    }
                }

                // Chame performClick para fins de acessibilidade
                v.performClick()
            }
            true
        }
    }




    private fun getPneusByCarId(carId: String): List<Pneu> {
        return listOf(
            Pneu().apply {
                id = "1"
                posicao = "Dianteiro Esquerdo"
                fabricante = "Michelin"
                aro = "16"
                ultimoEnchimento = "2024-11-01"
                ultimaTroca = "2024-10-01"
            },
            Pneu().apply {
                id = "2"
                posicao = "Traseiro Direito"
                fabricante = "Goodyear"
                aro = "15"
                ultimoEnchimento = "2024-11-05"
                ultimaTroca = "2024-09-15"
            }
        )
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

    private fun handleTireClick(tirePosition: String) {
        // Obtém o pneu correspondente à posição clicada
        val pneu = getPneusByCarId(prefs.idCarroSelected.toString()).find { it.posicao == tirePosition }
        if (pneu != null) {
            // Atualiza as informações exibidas na tela com os dados do pneu clicado
            binding.idPneu.text = "ID: ${pneu.id}"
            binding.fabricante.text = "Fabricante: ${pneu.fabricante}"
            binding.aro.text = "Aro: ${pneu.aro}"
            binding.ultimoEnchimento.text = "Último enchimento: ${pneu.ultimoEnchimento}"
            binding.ultimaTroca.text = "Última troca do pneu: ${pneu.ultimaTroca}"

            Toast.makeText(this, "Pneu ${pneu.posicao} clicado!", Toast.LENGTH_SHORT).show()
        } else {
            // Caso não seja encontrado um pneu para a posição clicada
            Toast.makeText(this, "Nenhum pneu encontrado para a posição: $tirePosition", Toast.LENGTH_SHORT).show()
        }
    }


}
