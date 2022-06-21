package com.example.parkingapp
import java.io.Serializable

data class compte(
    val id:Int,
    val nom:String,
    val prenom:String,
    val num_tlphn:String,
    val email:String,
    val mot_de_passe:String,
    val num_compte: String?,
    val mdp_compte: String?
):Serializable