package com.example.parkingapp

import java.io.Serializable

data class Parking(val id_parking:Int, val nom:String, val commune:String, val nbr_place_max:Int, val tarif_heure:Double,val parking_image:String, val voiture_image:String):Serializable