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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                val layoutManager = LinearLayoutManager(
                    this@VeiculosActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                recyclerView.layoutManager = layoutManager

                // Criação do adapter e atribuição ao RecyclerView
                val carAdapter = CarAdapter(cars) { selectedCar ->
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
                binding.recyclerViewCars.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
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
                            }
                        }
                    }
                })

                // Ação do botão "Verificar" para entrar na edição do veículo
                val buttonVerify = binding.buttonEdit
                buttonVerify.setOnClickListener {
                    val selectedCar = cars[currentPosition]

                    // Abrindo uma nova Activity com os dados do carro
                    val intent = Intent(this@VeiculosActivity, EditarVeiculoActivity::class.java)
                    intent.putExtra("CAR_NICKNAME", selectedCar.apelido)
                    intent.putExtra("CAR_MODEL", selectedCar.modelo)
                    intent.putExtra("CAR_ID", selectedCar.id)
                    startActivity(intent)
                }

                binding.buttonConsumptions.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            // Faz a chamada para buscar os veículos
                            val veiculos = serviceApi.getCars()

                            // Encontrar o veículo com o id selecionado
                            val carId = prefs.idCarroSelectedDelete
                            val vehicleEntry = veiculos.entries.find { it.value.id == carId }

                            if (vehicleEntry != null) {
                                val vehicleKey = vehicleEntry.key

                                // Deleta o veículo, sem verificar o response diretamente
                                val result = deleteVehicleSafely(vehicleKey)

                                // Verificando o resultado da operação
                                if (result.isSuccess) {
                                    // Sucesso na deleção
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            this@VeiculosActivity,
                                            "Veículo deletado com sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    // Se a deleção falhar
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            this@VeiculosActivity,
                                            "Erro ao deletar o veículo: ${result.exceptionOrNull()?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                // Caso não encontre o veículo
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@VeiculosActivity,
                                        "Nenhum veículo selecionado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            // Tratamento de erro para falha de comunicação
                            withContext(Dispatchers.Main) {
                                Log.e("deleter", "Erro: $e")
                                Toast.makeText(
                                    this@VeiculosActivity,
                                    "Erro ao deletar veículo: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    // Função para excluir o veículo com controle de exceção


                }
            } catch (e: Exception) {
                // Caso ocorra erro na requisição
                Toast.makeText(
                    this@VeiculosActivity,
                    "Erro ao carregar dados: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        binding.buttonVolta.setOnClickListener {
            finish()
        }
    }
    private suspend fun deleteVehicleSafely(vehicleKey: String): Result<Unit> {
        return try {
            // Faz a chamada para deletar o veículo
            serviceApi.deleteVeiculo(vehicleKey)
            Result.success(Unit)  // Sucesso
        } catch (e: Exception) {
            // Em caso de erro, retorna o erro
            Result.failure(e)
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
