package com.angellira.peregrino.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.R
import com.angellira.peregrino.model.Ocorrencias


class OcorrenciasAdapter(
    private val transactions: List<Ocorrencias>
) : RecyclerView.Adapter<OcorrenciasAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        private val tvValue: TextView = view.findViewById(R.id.tvValue)

        fun bind(transaction: Ocorrencias) {
            tvDescription.text = transaction.description

            val formattedValue = "R$ ${"%.2f".format(Math.abs(transaction.value))}"
            tvValue.text = if (transaction.isPositive) "+ $formattedValue" else "- $formattedValue"

            val valueColor = if (transaction.isPositive) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            tvValue.setTextColor(itemView.context.getColor(valueColor))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ocorrencia_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size
}
