package com.example.parkingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.viewmodel.AdvancedSearchModel

class DialogRateFragment: DialogFragment(R.layout.fragment_dialog_rate){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dialog_rate,container,false)
        rootView.findViewById<Button>(R.id.rate_cancel_button).setOnClickListener {
            dismiss()
        }


        val ratingStars = rootView.findViewById<RatingBar>(R.id.rating_bar)
        val cmntr = rootView.findViewById<EditText>(R.id.cmntr)
        if(ratingStars!=null){
            rootView.findViewById<Button>(R.id.rate_search_button).setOnClickListener {
                val msg = ratingStars.rating.toString()
                Toast.makeText(requireContext(),
                    "Note : "+msg +"\nCommentaire: " + cmntr.text.toString(), Toast.LENGTH_SHORT).show()

            }
        }

        return rootView
    }
}