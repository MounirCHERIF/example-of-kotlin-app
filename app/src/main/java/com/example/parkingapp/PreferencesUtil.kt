package com.example.menuapplication

import android.content.Context
import androidx.core.content.edit

const val fileName = "sharedPrefs"

fun saveConnexion(context: Context, connected:Boolean){
    val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.apply{
        putBoolean("Connected",connected)
    }.apply()
}

fun loadConnexion(context: Context):Boolean{
    val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("Connected",false)
}