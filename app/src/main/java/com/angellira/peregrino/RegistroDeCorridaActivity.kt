package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRegistrarCorridasBinding
import com.angellira.peregrino.databinding.ActivityRegistroDeCorridaBinding
import com.angellira.peregrino.model.Corrida
import com.angellira.peregrino.network.ApiServicePeregrino
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class RegistroDeCorridaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroDeCorridaBinding
    private lateinit var database: DatabaseReference
    private lateinit var nextCorridaTextView: TextView
    private lateinit var nextpontofinal: TextView
    private lateinit var nextvalor: TextView
    private val corridasApi = ApiServicePeregrino.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        loadNextCorrida()
        binding.buttonVolta.setOnClickListener {
            startActivity(Intent(this@RegistroDeCorridaActivity, MainActivity::class.java))
        }
        binding.buttonCorridas.setOnClickListener {
            startActivity(Intent(this@RegistroDeCorridaActivity, RegistrarCorridas::class.java))
        }
        binding.buttonRelatorios.setOnClickListener {
            startActivity(Intent(this@RegistroDeCorridaActivity, RelatoriosActivity::class.java))
        }
        database = FirebaseDatabase.getInstance().reference
        nextCorridaTextView = binding.nextCorrida
        nextpontofinal = binding.nextPontofinal
        nextvalor = binding.nextValor
    }

    private fun loadNextCorrida() {
        lifecycleScope.launch {
            try {
                val corridasMap = corridasApi.obterCorridas()

                Log.d("RegistroDeCorridaActivity", "Corridas recebidas: $corridasMap")

                val ultimaCorrida = corridasMap.values.maxByOrNull { it.data }

                if (ultimaCorrida != null) {
                    nextCorridaTextView.text = "Ponto inicial: ${ultimaCorrida.pontoInicial}"
                    nextpontofinal.text = "Ponto final: ${ultimaCorrida.pontoFinal}"
                    binding.nextPontofinal.visibility = VISIBLE
                    nextvalor.text = "Valor: ${ultimaCorrida.custo}"
                    binding.nextValor.visibility = VISIBLE
                } else {
                    Toast.makeText(
                        this@RegistroDeCorridaActivity,
                        "Não há corridas disponíveis.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Mostrar", "${e.message}")
                Toast.makeText(
                    this@RegistroDeCorridaActivity,
                    "Erro ao carregar corrida: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityRegistroDeCorridaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}