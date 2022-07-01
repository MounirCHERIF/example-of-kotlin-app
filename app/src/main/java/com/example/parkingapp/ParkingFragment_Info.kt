package com.example.parkingapp

import ReservationDialog
import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.menuapplication.loadConnexion
import com.example.parkingapp.data_class.Horaire
import com.example.parkingapp.retrofit.Endpoint
import com.example.parkingapp.viewmodel.HoraireModel
import com.example.parkingapp.viewmodel.ParkingModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ParkingFragment_Info : Fragment(R.layout.fragment_parking_info) {
    private val args: ParkingFragment_InfoArgs by navArgs()
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var horaireModel: HoraireModel



    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm= ViewModelProvider(requireActivity()).get(HoraireModel::class.java)

        setHasOptionsMenu(true);
        var cal = Calendar.getInstance()

        recyclerView = view?.findViewById(R.id.recyclerView_Horaire) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        progressBar = ProgressBar(context)
        recyclerView.layoutManager = layoutManager
        horaireModel = HoraireModel()
        horaireModel.data = vm.data


        tri_Days()

        var adapter = HoraireAdapter(context, horaireModel.data)
        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.rateButton).setOnClickListener {
            val action = ParkingFragment_InfoDirections.actionParkingFragmentInfoToDialogRateFragment()
            requireView().findNavController().navigate(action)
        }


        val image = view.findViewById(R.id.image) as ImageView
        val nom = view.findViewById(R.id.nom) as TextView
        val commune = view.findViewById(R.id.commune) as TextView
        val etat = view.findViewById(R.id.etat_detail) as TextView
        val taux = view.findViewById(R.id.taux) as TextView
        val distance = view.findViewById(R.id.distance) as TextView
        val temps = view.findViewById(R.id.temps) as TextView
        val tarif_heure = view.findViewById(R.id.tarif_heure) as TextView
        val voiture = view.findViewById(R.id.voiture) as ImageView
        val reservationButton = view.findViewById(R.id.reserver_button) as Button



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


        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        var bundle: Bundle = Bundle()
        bundle.putInt("idParking", args.idParking)

        var sharedPreferences = loadConnexion(requireActivity())
        bundle.putInt("idCompte", sharedPreferences.getString("idCompte", "")?.toInt()!!)

        reservationButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val reservationDialog: ReservationDialog = ReservationDialog()
                reservationDialog.arguments = bundle
                reservationDialog.show(childFragmentManager, ReservationDialog.TAG)
            }

        })
    }

    private fun tri_Days(){
        var  list: MutableList<Int> = MutableList(horaireModel.data.size) { 0 }
        for (horaire in horaireModel.data){
            when (horaire.jour) {
                "Samedi" -> list[horaireModel.data.indexOf(horaire)] = 0
                "Dimanche"-> list[horaireModel.data.indexOf(horaire)] = 1
                "Lundi"-> list[horaireModel.data.indexOf(horaire)] = 2
                "Mardi"-> list[horaireModel.data.indexOf(horaire)] = 3
                "Mercredi"-> list[horaireModel.data.indexOf(horaire)] = 4
                "Jeudi"-> list[horaireModel.data.indexOf(horaire)] = 5
                "Vendredi"-> list[horaireModel.data.indexOf(horaire)] = 6

            }
        }
        var tmp_horaire : Horaire
        for (i in 0..horaireModel.data.size-2){
            for (j in 0..horaireModel.data.size-i-2){
                if(list[j]>list[j+1]){
                    tmp_horaire = horaireModel.data[j]
                    horaireModel.data[j] = horaireModel.data[j+1]
                    horaireModel.data[j+1] = tmp_horaire
                }
            }
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        var item = menu.getItem(0)
        if (item != null) item.isVisible = false
        item = menu.getItem(1)
        if (item != null) item.isVisible = false
    }

}