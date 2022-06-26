package com.example.parkingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parkingapp.data_class.Horaire
import com.example.parkingapp.retrofit.Endpoint
import com.example.parkingapp.viewmodel.HoraireModel
import com.example.parkingapp.viewmodel.ParkingModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*

class ParkingFragment_Info : Fragment(R.layout.fragment_parking_info) {
    private val args: ParkingFragment_InfoArgs by navArgs()
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var horaireModel: HoraireModel


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm= ViewModelProvider(requireActivity()).get(HoraireModel::class.java)

        recyclerView = view?.findViewById(R.id.recyclerView_Horaire) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        progressBar = ProgressBar(context)
        recyclerView.layoutManager = layoutManager
        horaireModel = HoraireModel()
        horaireModel.data = vm.data
        //loadData(view)
        var adapter = HoraireAdapter(context, horaireModel.data)
        recyclerView.adapter = adapter


        val image = view.findViewById(R.id.image) as ImageView
        val nom = view.findViewById(R.id.nom) as TextView
        val commune = view.findViewById(R.id.commune) as TextView
        val etat = view.findViewById(R.id.etat_detail) as TextView
        val taux = view.findViewById(R.id.taux) as TextView
        val distance = view.findViewById(R.id.distance) as TextView
        val temps = view.findViewById(R.id.temps) as TextView
        val tarif_heure = view.findViewById(R.id.tarif_heure) as TextView
        val voiture = view.findViewById(R.id.voiture) as ImageView



        nom.text = args.nomParking
        commune.text = args.commune
        tarif_heure.text = args.tarifHeure + " DZD"
        etat.text = args.etat
        if (requireContext().applicationContext != null) {
            Glide.with(requireContext().applicationContext).load(args.imageParking).into(image)
            Glide.with(requireContext().applicationContext).load(R.drawable.car).into(voiture)
        }
        taux.text = args.taux
        distance.text = args.distance
        temps.text = args.temps


        // Set Maps button intent
        val mapsButton = view.findViewById(R.id.mapButton) as Button
        mapsButton.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=" + args.adresseParking+ "&mode=b")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

}