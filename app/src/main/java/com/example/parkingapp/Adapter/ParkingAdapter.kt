package com.example.parkingapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.navigation.fragment.findNavController


class ParkingAdapter(val context: Context?, var data: List<Parking>) :
    RecyclerView.Adapter<ParkingAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.parking_layout, parent, false)
        )

    }

    override fun getItemCount() = data.size

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bind(holder, data[position])
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bind(holder: MyViewHolder, parking: Parking) {

        holder.apply {
            nom.text = parking.nom
            commune.text = parking.commune
            taux.text = "- " + "65%"
            distance.text = "18 km"
            temps.text = "- " + "16 min"
            //if (parking.etat == true) {etat.text = "Ouvert"}else{etat.text = "Fermé"}
            if (context != null) {
                Glide.with(context).load(parking.parking_image).into(image)
                Glide.with(context).load(R.drawable.car).into(voiture)
            }
            itemView.setOnClickListener { view ->
                val image = parking.parking_image
                val nom = view.findViewById<TextView>(R.id.nom).text.toString()
                val commune = view.findViewById<TextView>(R.id.commune).text.toString()
                val action =
                    ParkingsList_FragmentDirections.actionParkingsListFragmentToParkingFragmentInfo(nom,commune,image)
                view.findNavController().navigate(action)
            }

        }

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById(R.id.image) as ImageView
        val nom = view.findViewById(R.id.nom) as TextView
        val commune = view.findViewById(R.id.commune) as TextView
        val etat = view.findViewById(R.id.etat) as TextView
        val voiture = view.findViewById(R.id.voiture) as ImageView
        val taux = view.findViewById(R.id.taux) as TextView
        val distance = view.findViewById(R.id.distance) as TextView
        val temps = view.findViewById(R.id.temps) as TextView

    }

}



