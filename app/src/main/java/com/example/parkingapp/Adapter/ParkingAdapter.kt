package com.example.parkingapp

import android.annotation.SuppressLint
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
import androidx.core.os.bundleOf
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.viewmodel.HoraireModel
import com.example.parkingapp.viewmodel.ParkingModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class ParkingAdapter(val context: Context?, var data: List<Parking>,var vm:HoraireModel) :
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

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bind(holder: MyViewHolder, parking: Parking) {

        holder.apply {
            nom.text = parking.nomParking
            commune.text = parking.commune
            distance.text = parking.distance
            temps.text = parking.temps
            etat.text = parking.etat
            var temp = 100
            if (parking.HistoriqueParking.size != 0){
                 temp = 100 * parking.HistoriqueParking[0].nombrePlace/parking.nombrePlaceMax
            }
            taux.text = "- " +temp.toString() + "%"
            if (context != null) {
                Glide.with(context).load(parking.photo).into(image)
                Glide.with(context).load(R.drawable.car).into(voiture)
            }
            itemView.setOnClickListener { view ->
                val image = parking.photo
                val nom = view.findViewById<TextView>(R.id.nom).text.toString()
                val commune = view.findViewById<TextView>(R.id.commune).text.toString()
                val tarif_heure = parking.tarifHeure
                val idParking = parking.idParking
                val adresse = parking.adresseParking.toString()
                val taux = view.findViewById<TextView>(R.id.taux).text.toString()
                val etat = parking.etat
                vm.data.clear()
                vm.data.addAll(parking.Horaire)

                val action =
                    ParkingsList_FragmentDirections.actionParkingsListFragmentToParkingFragmentInfo(nom,commune,image,tarif_heure.toString(),idParking,etat.toString(),taux,adresse,parking.distance,parking.temps)

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



