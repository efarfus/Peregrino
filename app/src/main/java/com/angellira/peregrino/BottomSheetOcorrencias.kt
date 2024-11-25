package com.angellira.peregrino

import android.content.DialogInterface
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
import androidx.lifecycle.lifecycleScope
import com.angellira.peregrino.databinding.ActivityBottomSheetOcorrenciasBinding
import com.angellira.peregrino.databinding.ActivityBottomSheetVeiculosBinding
import com.angellira.peregrino.databinding.ActivityOcorrenciasBinding
import com.angellira.peregrino.model.Ocorrencias
import com.angellira.peregrino.model.Veiculo
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileDialogOcorrenciasFragment : DialogFragment() {
    private lateinit var binding: ActivityBottomSheetOcorrenciasBinding
    lateinit var preferencesManager: Preferences
    private val ocorrencias = Ocorrencias()
    private val serviceApi = ApiServicePeregrino.retrofitService

    // Callback para notificar a Activity/Fragment chamadora
    private var onSaveListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityBottomSheetOcorrenciasBinding.inflate(inflater, container, false)
        preferencesManager = Preferences(requireContext())
        return binding.root
    }

    // Método para permitir que a Activity ou Fragment registre o callback
    fun setOnSaveListener(listener: () -> Unit) {
        onSaveListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.botaoconfirmaredicaoconta.setOnClickListener {
            ocorrencias.description = binding.textEditDescricao.text.toString()
            ocorrencias.value = binding.textEditValor.text.toString().trim()
            ocorrencias.isPositive = if (binding.valorPositivo.isChecked) true else false
            ocorrencias.date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            ocorrencias.carId = preferencesManager.idCarroSelected.toString()

            if (ocorrencias.description.isEmpty() || ocorrencias.value.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha pelo menos um campo!", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(IO) {
                    serviceApi.postOcorrencias(ocorrencias)
                    withContext(Main) {
                        Toast.makeText(requireContext(), "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        onSaveListener?.invoke() // Notifica a Activity/Fragment
                        dismiss()  // Fecha o DialogFragment
                    }
                }
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
