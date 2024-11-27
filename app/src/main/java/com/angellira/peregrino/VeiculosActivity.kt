package com.angellira.peregrino

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.adapter.CarAdapter
import com.angellira.peregrino.databinding.ActivityVeiculosBinding
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VeiculosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVeiculosBinding
    private val serviceApi = ApiServicePeregrino.retrofitService
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.buttonPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.buttonAdd.setOnClickListener {
            val editProfileDialogVeiculos = EditProfileDialogVeiculosFragment()
            // Exiba o DialogFragment
            editProfileDialogVeiculos.show(supportFragmentManager, "EditProfileDialogVeiculos")
        }

        lifecycleScope.launch {
            try {
                // Fazendo a requisição para obter a lista de carros
                val cars = serviceApi.getCars().values.toList()

                val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCars)
                val layoutManager = LinearLayoutManager(this@VeiculosActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager

                // Criação do adapter e atribuição ao RecyclerView
                val carAdapter = CarAdapter(cars) { selectedCar ->
                    Toast.makeText(this@VeiculosActivity, "Selecionado: ${selectedCar.apelido} - ${selectedCar.modelo}", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = carAdapter

                // Configuração do PagerSnapHelper para alinhar os itens de forma que apenas um item seja visível
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(recyclerView)

                // Configuração do indicador (como dots)
                val activeColor = resources.getColor(R.color.indicator_active_color, theme)
                val inactiveColor = resources.getColor(R.color.indicator_inactive_color, theme)

                val activeDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(activeColor)
                    setSize(20, 20)
                }

                val inactiveDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(inactiveColor)
                    setSize(12, 12)
                }

                // Cria os indicadores com base no tamanho da lista de carros
                binding.indicator.createIndicators(cars.size, 0)

                // Listener para sincronizar o indicador com o RecyclerView
                binding.recyclerViewCars.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val view = snapHelper.findSnapView(layoutManager)
                            val position = layoutManager.getPosition(view!!)
                            binding.indicator.animatePageSelected(position)
                        }
                    }
                })

                // Definindo a variável currentPosition para saber qual item está sendo visualizado
                var currentPosition = 0

                // Listener para monitorar o item selecionado
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val view = snapHelper.findSnapView(layoutManager)
                            view?.let {
                                currentPosition = layoutManager.getPosition(it)
                                val currentCar = cars[currentPosition]
                                prefs.idCarroSelectedDelete = currentCar.id
                                Toast.makeText(
                                    recyclerView.context,
                                    "Atual: ${currentCar.apelido} - ${currentCar.modelo}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })

                // Ação do botão "Verificar" para entrar na edição do veículo
                val buttonVerify = binding.buttonEdit
                buttonVerify.setOnClickListener {
                    val selectedCar = cars[currentPosition]
                    Toast.makeText(
                        this@VeiculosActivity,
                        "Entrando no veículo: ${selectedCar.apelido} - ${selectedCar.modelo}",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Abrindo uma nova Activity com os dados do carro
                    val intent = Intent(this@VeiculosActivity, EditarVeiculoActivity::class.java)
                    intent.putExtra("CAR_NICKNAME", selectedCar.apelido)
                    intent.putExtra("CAR_MODEL", selectedCar.modelo)
                    intent.putExtra("CAR_ID", selectedCar.id)
                    startActivity(intent)
                }

                binding.buttonConsumptions.setOnClickListener {
                    lifecycleScope.launch {
                        try {
                            // Suponha que você tenha o ID do veículo
                            val vehicleId = "94677e88-6383-4df8-b679-1371c1c16e96"

                            // Crie uma referência ao nó dos veículos
                            val vehiclesRef = Firebase.database.getReference("Veiculos")

                            // Procure pelo veículo que tenha o id correspondente
                            val snapshot = vehiclesRef.orderByChild("id").equalTo(vehicleId).get().await()

                            if (snapshot.exists()) {
                                // O veículo foi encontrado, agora você pode pegar a chave do nó
                                val vehicleNodeKey = snapshot.children.first().key
                                if (vehicleNodeKey != null) {
                                    try {
                                        // Agora você tem a chave do nó, e pode fazer a requisição de exclusão
                                        val deletedCar = serviceApi.deleteVeiculo(vehicleNodeKey)
                                        Toast.makeText(this@VeiculosActivity, "Veículo deletado", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        // Caso ocorra erro ao deletar o veículo
                                        Toast.makeText(this@VeiculosActivity, "Erro ao deletar o veículo: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this@VeiculosActivity, "Veículo não encontrado", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@VeiculosActivity, "Veículo não encontrado", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            // Caso ocorra erro ao buscar dados no Firebase
                            Toast.makeText(this@VeiculosActivity, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                // Caso ocorra erro na requisição
                Toast.makeText(this@VeiculosActivity, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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
