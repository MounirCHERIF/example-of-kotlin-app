package com.example.parkingapp

import com.example.parkingapp.data_class.HistoriqueParking
import java.io.Serializable

data class Parking(
    val idParking: Int,
    val nomParking: String,
    val commune: String,
    val adresseParking: String,
    val nombrePlaceMax: Int,
    val tarifHeure: Int,
    val photo: String,
    val voiture_image: String,
    val HistoriqueParking:MutableList<HistoriqueParking>,
    var etat : String?
) : Serializable