package com.angellira.peregrino

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.angellira.peregrino.databinding.ActivityBottomSheetOcorrenciasBinding
import com.angellira.peregrino.databinding.ActivityOcorrenciasBinding
import com.angellira.reservafrotas.preferences.Preferences

class EditProfileDialogOcorrenciasFragment : DialogFragment() {
    private lateinit var binding: ActivityBottomSheetOcorrenciasBinding

    lateinit var preferencesManager: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferencesManager = Preferences(requireContext())
        return inflater.inflate(R.layout.activity_bottom_sheet_ocorrencias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityBottomSheetOcorrenciasBinding.inflate(layoutInflater)


        val buttonSave = view.findViewById<Button>(R.id.botaoconfirmaredicaoconta)

        binding.botaoconfirmaredicaoconta.setOnClickListener {
            val combustivel = binding.textEditCombustivel.text.toString().trim()
            val manutencao = binding.textEditManutencao.text.toString().trim()
            val pneus = binding.textEditPneus.text.toString().trim()
            val limpeza = binding.textEditLimpeza.text.toString().trim()
            val gorjeta = binding.textEditGorjeta.text.toString().trim()
            val lucro = binding.textEditLucro.text.toString().trim()

            if (combustivel.isEmpty() && manutencao.isEmpty() && pneus.isEmpty() &&
                limpeza.isEmpty() && gorjeta.isEmpty() && lucro.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Preencha pelo menos um campo!", Toast.LENGTH_SHORT).show()
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