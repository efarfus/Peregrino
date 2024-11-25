package com.angellira.peregrino.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.R
import com.angellira.peregrino.model.Car
import com.angellira.peregrino.model.Veiculo

class CarAdapter(
    private val cars: List<Veiculo>, // Agora, a lista de Veiculo é passada corretamente
    private val onClick: (Veiculo) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carImage: ImageView = itemView.findViewById(R.id.imageCar)
        private val carNickname: TextView = itemView.findViewById(R.id.textApelido)
        private val carModel: TextView = itemView.findViewById(R.id.textCarModel)

        fun bind(car: Veiculo) {
            carImage.setImageResource(R.drawable.carro) // Exemplo: ícone do carro
            carNickname.text = car.apelido  // Acessando o apelido do veículo
            carModel.text = car.modelo      // Acessando o modelo do veículo
            itemView.setOnClickListener { onClick(car) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount() = cars.size
}
