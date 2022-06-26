package com.example.parkingapp

import com.example.parkingapp.data_class.HistoriqueParking
import com.example.parkingapp.data_class.Horaire
import java.io.Serializable

data class Parking(
    val idParking: Int,
    val nomParking: String,
    val commune: String,
    val adresseParking: String,
    val latitude: String,
    val longitude: String,
    val nombrePlaceMax: Int,
    val tarifHeure: Int,
    val photo: String,
    val voiture_image: String,
    val HistoriqueParking:MutableList<HistoriqueParking>,
    val Horaire:MutableList<Horaire>,
    var etat : String?,
    var distance: String,
    var temps: String
) : Serializable