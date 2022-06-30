package com.example.parkingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.menuapplication.saveConnexion

class DeconnexionFragment : Fragment(R.layout.fragment_deconnexion) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        saveConnexion(requireContext(),false,"","","","","")
        val intent = Intent(requireContext(), SignActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}