package com.angellira.peregrino.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angellira.peregrino.R
import com.angellira.peregrino.model.Car

class CarAdapter(
    private val cars: List<Car>, // Use uma classe Car em vez de apenas strings
    private val onClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carImage: ImageView = itemView.findViewById(R.id.imageCar)
        private val carNickname: TextView = itemView.findViewById(R.id.textApelido)
        private val carModel: TextView = itemView.findViewById(R.id.textCarModel)

        fun bind(car: Car) {
            carImage.setImageResource(R.drawable.carro) // Exemplo: `R.drawable.carro`
            carNickname.text = car.nickname
            carModel.text = car.model
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
