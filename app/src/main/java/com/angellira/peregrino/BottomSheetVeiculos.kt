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
import com.angellira.peregrino.databinding.ActivityBottomSheetOcorrenciasBinding
import com.angellira.peregrino.databinding.ActivityBottomSheetVeiculosBinding
import com.angellira.reservafrotas.preferences.Preferences

class EditProfileDialogVeiculosFragment : DialogFragment() {

    private lateinit var binding: ActivityBottomSheetVeiculosBinding

    lateinit var preferencesManager: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferencesManager = Preferences(requireContext())
        return inflater.inflate(R.layout.activity_bottom_sheet_veiculos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityBottomSheetVeiculosBinding.inflate(layoutInflater)


        val buttonSave = view.findViewById<Button>(R.id.botaoconfirmaredicaoconta)

        binding.botaoconfirmaredicaoconta.setOnClickListener {
            val apelido = binding.textEditApelido.text.toString()
            val modelo = binding.textEditModelo.text.toString()

            if (apelido.isEmpty() || modelo.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                // Realiza o registro
                Toast.makeText(requireContext(), "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                // Adicione sua lógica de registro aqui
            }
        }


        val options = listOf("Diantero esquerdo", "Diantero direito", "Traseiro esquerdo", "Traseiro direito")


    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}