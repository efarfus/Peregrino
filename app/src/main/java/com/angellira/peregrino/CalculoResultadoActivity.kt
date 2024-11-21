package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityCalculoPasso3Binding
import com.angellira.peregrino.databinding.ActivityCalculoPasso4Binding
import com.angellira.peregrino.databinding.ActivityCalculoResultadoBinding
import com.angellira.reservafrotas.preferences.Preferences
import java.text.DecimalFormat

class CalculoResultadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculoResultadoBinding
    private val prefs by lazy { Preferences(this) }


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
            startActivity(Intent(this, ConsumoCombustivelActivity::class.java))
            finishAffinity()
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