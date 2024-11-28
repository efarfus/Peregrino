package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRegistrarCorridasBinding
import com.angellira.peregrino.model.Corrida
import com.angellira.peregrino.network.ApiServicePeregrino
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class RegistrarCorridas : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarCorridasBinding
    private lateinit var database: DatabaseReference
    private val corridasApi = ApiServicePeregrino.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference

        setupView()
        binding.buttonVolta.setOnClickListener {
            startActivity(Intent(this, RegistroDeCorridaActivity::class.java))
        }
        logicaPrincipal()
    }

    private fun logicaPrincipal() {
        val custos = binding.custoviageminput
        val pontoInicial = binding.pontoinicialinput
        val pontofinal = binding.pontofinalinput
        val registroButton = binding.buttonRegistrar

        registroButton.setOnClickListener {
            var isValid = true

            if (custos.text.isNullOrEmpty()) {
                custos.error = "Custo é obrigatório"
                isValid = false
            }

            if (pontoInicial.text.isNullOrEmpty()) {
                pontoInicial.error = "Ponto inicial é obrigatório"
                isValid = false
            }

            if (pontofinal.text.isNullOrEmpty()) {
                pontofinal.error = "Ponto final é obrigatório"
                isValid = false
            }

            if (isValid) {

                val currentDate = getCurrentDate()


                val novaCorrida = Corrida(
                    id = UUID.randomUUID().toString(),
                    custo = custos.text.toString(),
                    pontoInicial = pontoInicial.text.toString(),
                    pontoFinal = pontofinal.text.toString(),
                    data = currentDate
                )

                lifecycleScope.launch(Dispatchers.IO){
                    try{
                    corridasApi.registrarCorrida(novaCorrida)
                        withContext(Main){
                        Toast.makeText(
                            this@RegistrarCorridas,
                            "Corrida registrada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()}
                        startActivity(Intent(this@RegistrarCorridas, RegistroDeCorridaActivity::class.java))
                    } catch (e: Exception){
                        withContext(Main){
                        Toast.makeText(this@RegistrarCorridas, "Erro ao registrar corrida", Toast.LENGTH_SHORT).show()
                    }
                    }
                }
            }
        }

        custos.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val locale = Locale("pt", "BR")
            private val currencyFormat = NumberFormat.getCurrencyInstance(locale)

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return
                isUpdating = true

                try {
                    val cleanString = s.toString().replace(Regex("[R$,.\\s]"), "")

                    val parsed = cleanString.toDoubleOrNull() ?: 0.0
                    val formatted = currencyFormat.format(parsed / 100)

                    custos.setText(formatted)
                    custos.setSelection(formatted.length)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                isUpdating = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }


    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityRegistrarCorridasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}