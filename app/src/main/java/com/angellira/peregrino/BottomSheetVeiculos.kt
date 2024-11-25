package com.angellira.peregrino

import android.os.Bundle
import android.util.Log
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
import com.angellira.peregrino.databinding.ActivityBottomSheetOcorrenciasBinding
import com.angellira.peregrino.databinding.ActivityBottomSheetVeiculosBinding
import com.angellira.peregrino.model.Veiculo
import com.angellira.peregrino.network.ApiServicePeregrino
import com.angellira.reservafrotas.preferences.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class EditProfileDialogVeiculosFragment : DialogFragment() {

    private lateinit var binding: ActivityBottomSheetVeiculosBinding
    private val serviceApi = ApiServicePeregrino.retrofitService
    lateinit var preferencesManager: Preferences
    private val veiculo = Veiculo()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityBottomSheetVeiculosBinding.inflate(inflater, container, false)
        preferencesManager = Preferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.botaoconfirmaredicaoconta.setOnClickListener {
            val apelido = binding.textEditApelido.text.toString()
            val modelo = binding.textEditModelo.text.toString()
            veiculo.id = UUID.randomUUID().toString()
            veiculo.modelo = modelo
            veiculo.apelido = apelido

            if (apelido.isEmpty() || modelo.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {

                lifecycleScope.launch(IO) {
                    try {
                        val response = serviceApi.postVeiculos(veiculo)
                        if (response.isSuccessful) {
                            Log.d("burna", "Requisição bem-sucedida: ${response.body()}")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                dismiss()
                            }
                        } else {
                            Log.e("burna", "Erro na requisição: ${response.code()} - ${response.message()}")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Erro: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("burna", "Exceção na requisição: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Falha ao conectar com o servidor.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Toast.makeText(requireContext(), "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
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