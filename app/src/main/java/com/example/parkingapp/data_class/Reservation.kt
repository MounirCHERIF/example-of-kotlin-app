package com.example.parkingapp.data_class

import java.io.Serializable
import java.util.*

data class Reservation(
    val numReservation  : Int,
    val dateReservation : Date,
    val heureEntre      :String,
    val heureSortie     :String,
    val numPlaceParking :String,
    val paye            :Boolean,
    val idParking       :Int?,
    val idCompte        :Int?,

): Serializable
