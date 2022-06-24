package com.example.parkingapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.data_class.Parking_Horaire
import com.example.parkingapp.retrofit.Endpoint
import com.example.parkingapp.viewmodel.ParkingModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ParkingsList_Fragment : Fragment(R.layout.fragment_parkings_list) {
    lateinit var parkingModel: ParkingModel
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view?.findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        progressBar = ProgressBar(context)
        recyclerView.layoutManager = layoutManager
        parkingModel = ParkingModel()
        loadData()
        var adapter = ParkingAdapter(context,parkingModel.data)
        recyclerView.adapter = adapter
    }

    fun loadData(){
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            requireActivity().runOnUiThread() {
                progressBar.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE

                Toast.makeText(requireActivity(), throwable.message, Toast.LENGTH_SHORT).show()
            }

        }
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE

        CoroutineScope(Dispatchers.IO+ exceptionHandler).launch {
            val response = Endpoint.createEndpoint().getAllParkings()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    parkingModel.data = response.body()!!.toMutableList()
                        val response = Endpoint.createEndpoint().getHorairesParkings(getCurrentDay())
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful && response.body() != null) {
                                var list = response.body()!!.toMutableList()
                                for(parking in parkingModel.data){
                                    parking.etat = "Fermé"
                                }
                                for (element in list) {
                                    for(parking in parkingModel.data){
                                        if(element.Associer[0].parkingIdParking==parking.idParking){
                                            val sdf = SimpleDateFormat("HH:mm")
                                            val currentDate = sdf.format(Date())
                                            val time1 = currentDate.subSequence(0,2).toString().toInt() * 60 +currentDate.subSequence(3,5).toString().toInt()
                                            val time2 = element.horaireOuverture.subSequence(11,13).toString().toInt() * 60 +element.horaireOuverture.subSequence(14,16).toString().toInt()
                                            val time3 = element.horaireFermeture.subSequence(11,13).toString().toInt() * 60 +element.horaireFermeture.subSequence(14,16).toString().toInt()
                                            if(time1>=time2 && time1<=time3){
                                                parking.etat = "Ouvert"
                                            }
                                        }
                                    }
                                }
                                recyclerView.visibility = View.VISIBLE
                            }else {
                                Toast.makeText(requireActivity(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
                            }
                        }
                    recyclerView.adapter = ParkingAdapter(requireActivity(), parkingModel.data)
                } else {
                    Toast.makeText(requireActivity(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDay():String{
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        var jour:String =""
        when (day) {
            Calendar.SUNDAY -> {jour="Dimanche"}
            Calendar.MONDAY -> {jour= "Lundi"}
            Calendar.TUESDAY -> {jour= "Mardi"}
            Calendar.WEDNESDAY -> {jour= "Mercredi"}
            Calendar.THURSDAY -> {jour= "Jeudi"}
            Calendar.FRIDAY -> {jour= "Vendredi"}
            Calendar.SATURDAY -> {jour= "Samedi"}
        }
        return jour
    }
}