package com.angellira.peregrino

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.adapter.CarAdapter
import com.angellira.peregrino.databinding.ActivityVeiculosBinding
import com.angellira.peregrino.model.Car

class VeiculosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVeiculosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        val cars = listOf(
            Car("Foguetinho", "Gol GTI 1998"),
            Car("Trovão Azul", "Uno Mille 2005"),
            Car("Relâmpago", "Civic 2010")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCars)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CarAdapter(cars) { selectedCar ->
            Toast.makeText(this, "Selecionado: ${selectedCar.nickname} - ${selectedCar.model}", Toast.LENGTH_SHORT).show()
        }
        val snapHelper = PagerSnapHelper() // Alinha um item de cada vez
        snapHelper.attachToRecyclerView(recyclerView)

        val activeColor = resources.getColor(R.color.indicator_active_color, theme) // Cor ativa
        val inactiveColor = resources.getColor(R.color.indicator_inactive_color, theme) // Cor inativa

        // Drawable para o indicador ativo
        val activeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(activeColor) // Cor ativa
            setSize(20, 20) // Tamanho maior para ativo
        }

        // Drawable para o indicador inativo
        val inactiveDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(inactiveColor) // Cor inativa
            setSize(12, 12) // Tamanho menor para inativo
        }

        // Cria o indicador com base na quantidade de carros
        binding.indicator.createIndicators(cars.size, 0)

        // Adiciona o listener para sincronizar o indicador com o RecyclerView
        binding.recyclerViewCars.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val view = snapHelper.findSnapView(layoutManager)
                    val position = layoutManager.getPosition(view!!)
                    binding.indicator.animatePageSelected(position) // Atualiza a animação
                }
            }
        })

        var currentPosition = 0

        // Listener para monitorar o item selecionado
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // Parado após rolagem
                    val view = snapHelper.findSnapView(layoutManager)
                    view?.let {
                        currentPosition = layoutManager.getPosition(it)
                        val currentCar = cars[currentPosition]
                        Toast.makeText(
                            recyclerView.context,
                            "Atual: ${currentCar.nickname} - ${currentCar.model}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        val buttonVerify = binding.buttonEdit
        buttonVerify.setOnClickListener {
            val selectedCar = cars[currentPosition]
            Toast.makeText(
                this,
                "Entrando no veículo: ${selectedCar.nickname} - ${selectedCar.model}",
                Toast.LENGTH_SHORT
            ).show()

            // Aqui você pode abrir uma nova Activity ou realizar uma ação específica
            val intent = Intent(this, EditarVeiculoActivity::class.java)
            intent.putExtra("CAR_NICKNAME", selectedCar.nickname)
            intent.putExtra("CAR_MODEL", selectedCar.model)
            startActivity(intent)
        }

        binding.buttonVolta.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityVeiculosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonEdit.setOnClickListener {
            startActivity(Intent(this, EditarVeiculoActivity::class.java))
        }
    }
}
