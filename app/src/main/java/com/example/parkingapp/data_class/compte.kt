package com.example.parkingapp
import java.io.Serializable

data class compte(
    val idCompte:Int,
    val nom:String,
    val prenom:String,
    val numTelephone:String,
    val email:String,
    val motDePasse:String,
    val numCompte: String?,
    val motDePasseCompte: String?
):Serializable