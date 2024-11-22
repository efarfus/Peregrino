package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityEditarVeiculoBinding
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRelatoriosBinding
import com.angellira.reservafrotas.preferences.Preferences

class EditarVeiculoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarVeiculoBinding
    private val prefs by lazy { Preferences(this) }

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

        val carNickname = intent.getStringExtra("CAR_NICKNAME")
        val carModel = intent.getStringExtra("CAR_MODEL")
        prefs.idCarroSelected = intent.getStringExtra("CAR_ID")

        binding.textApelidoCarro.text = carNickname
        binding.textModeloCarro.text = carModel
        Log.e("bruna", prefs.idCarroSelected.toString())
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