package com.angellira.peregrino

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.adapter.HistoricoConsumoAdapter
import com.angellira.peregrino.databinding.ActivityHistoricoConsumoBinding
import com.angellira.peregrino.model.HistoricoConsumo
import com.angellira.peregrino.model.Veiculo
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoricoConsumoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricoConsumoBinding
    private lateinit var adapter: HistoricoConsumoAdapter
    private lateinit var historicoList: MutableList<HistoricoConsumo>
    private var historico = HistoricoConsumo("", "", "")
    private val serviceApi = ApiServicePeregrino.retrofitService
    private val prefs by lazy { Preferences(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        lifecycleScope.launch(IO){

            val carlist = getCarByInternalId(prefs.idCarroSelected.toString())
            historico.id = carlist!!.id
            historico.data = carlist.dataMediaFeita
            historico.eficiencia = carlist.mediaVeiculo
            withContext(Main){
                recyclerView()
            }

        }
    }

    private suspend fun getCarByInternalId(id: String): Veiculo? {
        val response = serviceApi.getCars()
        return response.values.find { it.id == id }
    }

    private fun recyclerView() {
        binding.recyclerViewHistorico.layoutManager = LinearLayoutManager(this)



        adapter = HistoricoConsumoAdapter(historicoList)
        binding.recyclerViewHistorico.adapter = adapter
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityHistoricoConsumoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}