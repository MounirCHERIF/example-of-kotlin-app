package com.example.parkingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.menuapplication.saveConnexion
import com.example.parkingapp.retrofit.Endpoint
import kotlinx.coroutines.*


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        view.findViewById<Button>(R.id.login).setOnClickListener {

            var email: String = view.findViewById<TextView>(R.id.email).text.toString()
            var pwd: String =view.findViewById<TextView>(R.id.password).text.toString()

            // Inserting Coroutine
            val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
                val text = "Problème de connextion"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
                throwable.printStackTrace()
            }
            CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
                val response = Endpoint.createEndpoint().getCompteByEmail(email)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!

                        // Verify password
                        if (data.motDePasse == pwd) {
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, "Vous êtes connecté", duration)
                            toast.show()

                            saveConnexion(requireContext(), true)
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            val text = "Mot de Passe erroné"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, text, duration)
                            toast.show()
                        }
                    }
                    else {
                        val text = "Compte inexistant"
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, text, duration)
                        toast.show()
                    }
                }
            }

            var signUp_textView = view.findViewById(R.id.signInTextView) as TextView
            signUp_textView.setOnClickListener{
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }

    }
}