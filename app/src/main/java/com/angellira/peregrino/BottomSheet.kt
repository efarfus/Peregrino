package com.angellira.peregrino

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityBottomSheetBinding
import com.angellira.peregrino.databinding.ActivityBottomSheetOcorrenciasBinding
import com.angellira.peregrino.model.Pneu
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class EditProfileDialogFragment : DialogFragment() {
    private lateinit var binding: ActivityBottomSheetBinding
    private val serviceApi = ApiServicePeregrino.retrofitService
    lateinit var preferencesManager: Preferences
    private val pneu = Pneu()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityBottomSheetBinding.inflate(inflater, container, false)
        preferencesManager = Preferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSave = view.findViewById<Button>(R.id.botaoconfirmaredicaoconta)

        buttonSave.setOnClickListener {
            pneu.aro = binding.textEditAro.text.toString()
            pneu.ultimaTroca = binding.textEditTroca.text.toString()
            pneu.ultimoEnchimento = binding.textEditEnchimento.text.toString()
            pneu.fabricante = binding.textEditFabricante.text.toString()
            pneu.carId = preferencesManager.idCarroSelected.toString()
            pneu.id = UUID.randomUUID().toString()

            lifecycleScope.launch(IO){
                serviceApi.postPneus(pneu)
                withContext(Main){
                    dismiss()
                }
            }
        }


        val options = listOf("Dianteiro Esquerdo", "Dianteiro Direito", "Traseiro Esquerdo", "Traseiro Direito")

        // Obtendo o Spinner
        val spinner: Spinner = view.findViewById<Spinner>(R.id.spinner)

        // Criando o ArrayAdapter
        val adapter = ArrayAdapter(
            requireContext(),  // Contexto
            android.R.layout.simple_spinner_item,  // Layout de item do Spinner
            options  // Dados
        )

        // Configurando o layout do dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Atribuindo o adapter ao Spinner
        spinner.adapter = adapter

        // Definindo o listener para capturar a seleção
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = options[position]
                pneu.posicao = selectedOption
                Toast.makeText(requireContext(), "Selecionado: $selectedOption", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ação quando nenhum item for selecionado
            }
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}