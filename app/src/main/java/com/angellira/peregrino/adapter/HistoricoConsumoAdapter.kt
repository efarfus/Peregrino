package com.angellira.peregrino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.R
import com.angellira.peregrino.model.HistoricoConsumo

class HistoricoConsumoAdapter(private val historicoList: List<HistoricoConsumo>) :
    RecyclerView.Adapter<HistoricoConsumoAdapter.HistoricoConsumoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoConsumoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historico_consumo, parent, false)
        return HistoricoConsumoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoConsumoViewHolder, position: Int) {
        val historico = historicoList[position]
        holder.dataTextView.text = "Data: ${historico.data}"
        holder.eficienciaTextView.text = "Eficiência: ${historico.eficiencia} km/l"
    }

    override fun getItemCount(): Int {
        return historicoList.size
    }

    inner class HistoricoConsumoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataTextView: TextView = itemView.findViewById(R.id.textViewData)
        val eficienciaTextView: TextView = itemView.findViewById(R.id.textViewEficiencia)
    }
}