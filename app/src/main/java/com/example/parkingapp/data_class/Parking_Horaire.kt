package com.example.parkingapp.data_class

import java.io.Serializable

data class Parking_Horaire(
    val idHoraire: Int,
    val horaireOuverture: String,
    val horaireFermeture: String,
    val jour: String,
    val Associer: MutableList<Associer>
) : Serializable
