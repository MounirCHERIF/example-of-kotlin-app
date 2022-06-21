package com.example.parkingapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

class ParkingFragment_Info : Fragment(R.layout.fragment_parking_info) {
    private val args :ParkingFragment_InfoArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image = view.findViewById(R.id.image) as ImageView
        val nom = view.findViewById(R.id.nom) as TextView
        val commune = view.findViewById(R.id.commune) as TextView
        val etat = view.findViewById(R.id.etat) as TextView
        val taux =  view.findViewById(R.id.taux) as TextView
        val distance = view.findViewById(R.id.distance) as TextView
        val temps = view.findViewById(R.id.temps) as TextView
        //val tarif_heure = view.findViewById(R.id.tarif_heure) as TextView
        val voiture = view.findViewById(R.id.voiture) as ImageView

        nom.text = args.nomParking
        commune.text = args.commune
        //tarif_heure.text = parking.tarif_heure.toString() + " DA"
        //if (parking.etat == true) {etat.text = "Ouvert"}else{etat.text = "Fermé"}
        if (requireContext().applicationContext != null) {
            Glide.with(requireContext().applicationContext).load(args.imageParking).into(image)
            Glide.with(requireContext().applicationContext).load(R.drawable.car).into(voiture)
        }
        taux.text = "- " + "65%"
        distance.text = "18 km"
        temps.text = "- " + "16 min"


        // Set Maps button intent
        val mapsButton = view.findViewById(R.id.mapButton) as Button
        mapsButton.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=" + "30.002" + "," + "2.666" + "&mode=b")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}