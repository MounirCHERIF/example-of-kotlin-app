package com.example.parkingapp.data_class

import java.sql.Time

data class Horaire(
    val idHoraire: Int,
    val horaireOuverture: String,
    val horaireFermeture: String,
    val jour: String
)
