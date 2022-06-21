package com.example.parkingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.retrofit.Endpoint
import com.example.parkingapp.viewmodel.ParkingModel
import kotlinx.coroutines.*

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
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            requireActivity().runOnUiThread() {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireActivity(), throwable.message, Toast.LENGTH_SHORT).show()
            }

        }
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO+ exceptionHandler).launch {
            val response = Endpoint.createEndpoint().getAllParkings()
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful && response.body() != null) {
                    parkingModel.data = response.body()!!.toMutableList()
                    recyclerView.adapter = ParkingAdapter(requireActivity(), parkingModel.data)
                } else {
                    Toast.makeText(requireActivity(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}