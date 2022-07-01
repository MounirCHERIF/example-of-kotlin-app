package com.example.parkingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.viewmodel.AdvancedSearchModel

class DialogSearchFragment: DialogFragment(R.layout.fragment_dialog_search){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dialog_search,container,false)
        rootView.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dismiss()
        }


        val EditText1 = rootView.findViewById<EditText>(R.id.et_prixMax)
        val EditText2 = rootView.findViewById<EditText>(R.id.et_distanceMax)
        rootView.findViewById<Button>(R.id.search_button).setOnClickListener {
            if(EditText1.text.toString() =="" || EditText2.text.toString() == "") {
                Toast.makeText(requireActivity(), "Vous devez remplir tous les champs", Toast.LENGTH_SHORT).show()

            }else{
                val vm= ViewModelProvider(requireActivity()).get(AdvancedSearchModel::class.java)
                vm.prix = EditText1.text.toString()
                vm.distance = EditText2.text.toString()

                val action =
                    DialogSearchFragmentDirections.actionDialogSearchFragment2ToParkingsListFragment()
                findNavController().navigate(action)
            }
        }

        return rootView
    }
}