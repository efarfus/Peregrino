package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.peregrino.databinding.ActivityMainBinding
import com.angellira.peregrino.databinding.ActivityRegistrarCorridasBinding
import com.angellira.peregrino.databinding.ActivityRegistroDeCorridaBinding
import com.angellira.peregrino.model.Corrida
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegistroDeCorridaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroDeCorridaBinding
    private lateinit var database: DatabaseReference
    private lateinit var nextCorridaTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        binding.buttonVolta.setOnClickListener{
            startActivity(Intent(this@RegistroDeCorridaActivity, MainActivity::class.java))
        }
        binding.buttonCorridas.setOnClickListener{
            startActivity(Intent(this@RegistroDeCorridaActivity, RegistrarCorridas::class.java))
        }
        binding.buttonRelatorios.setOnClickListener{
            startActivity(Intent(this@RegistroDeCorridaActivity, RelatoriosActivity::class.java))
        }
        database = FirebaseDatabase.getInstance().reference
        nextCorridaTextView = binding.nextCorrida
        loadNextCorrida()
    }

    private fun loadNextCorrida() {
        database.child("corridas").orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val corrida = child.getValue(Corrida::class.java)
                            corrida?.let {
                                nextCorridaTextView.text = """
                                    Próxima Corrida:
                                    ID: ${it.id}
                                    Custo: ${it.custo}
                                    Ponto Inicial: ${it.pontoInicial}
                                    Ponto Final: ${it.pontoFinal}
                                """.trimIndent()
                            }
                        }
                    } else {
                        nextCorridaTextView.text = "Nenhuma corrida cadastrada."
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RegistroDeCorridaActivity, "Erro ao carregar dados!", Toast.LENGTH_SHORT).show()
                }
            })
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