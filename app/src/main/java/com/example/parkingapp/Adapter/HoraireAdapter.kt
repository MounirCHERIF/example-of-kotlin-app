package com.example.parkingapp

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.data_class.Horaire


class HoraireAdapter(val context: Context?, var data: List<Horaire>) :
    RecyclerView.Adapter<HoraireAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.horaire_layout, parent, false)
        )

    }

    override fun getItemCount() = data.size

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: HoraireAdapter.MyViewHolder, position: Int) {
        bind(holder, data[position])
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bind(holder: HoraireAdapter.MyViewHolder, horaire: Horaire) {

        holder.apply {
            jour.text = horaire.jour
            ouverture.text = horaire.horaireOuverture.subSequence(11,16).toString() + " à "
            fermuture.text = horaire.horaireFermeture.subSequence(11,16)
        }

    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val jour = view.findViewById(R.id.jour) as TextView
        val ouverture = view.findViewById(R.id.horaire_debut) as TextView
        val fermuture = view.findViewById(R.id.horaire_fin) as TextView
    }

}

