package com.angellira.peregrino

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
import com.angellira.reservafrotas.preferences.Preferences

class EditProfileDialogFragment : DialogFragment() {

    lateinit var preferencesManager: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferencesManager = Preferences(requireContext())
        return inflater.inflate(R.layout.activity_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSave = view.findViewById<Button>(R.id.botaoconfirmaredicaoconta)

        buttonSave.setOnClickListener {

        }


        val options = listOf("Diantero Esquerdo", "Diantero Direito", "Traseiro Esquerdo", "Traseiro Direito")

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