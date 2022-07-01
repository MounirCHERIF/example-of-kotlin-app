package com.example.parkingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.menuapplication.saveConnexion
import com.example.parkingapp.retrofit.Endpoint
import kotlinx.coroutines.*

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private var nom: EditText? = null
    private var prenom: EditText? = null
    private var num_tlphn: EditText? = null
    private var email: EditText? = null
    private var mot_de_passe: EditText? = null
    private var btn: Button? = null
    private var loadingPB: ProgressBar? = null
    private var responseTV: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nom = view.findViewById<View>(R.id.nom) as EditText
        prenom = view.findViewById<View>(R.id.prenom) as EditText
        num_tlphn = view.findViewById<View>(R.id.num_tlphn) as EditText
        email = view.findViewById<View>(R.id.email) as EditText
        mot_de_passe = view.findViewById<View>(R.id.password) as EditText
        btn = view.findViewById<View>(R.id.signUp) as Button
        loadingPB = view.findViewById(R.id.loadingPB) as ProgressBar
        responseTV = view.findViewById(R.id.TVResponse) as TextView

        // adding on click listener to our button.
        btn!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (nom!!.getText().toString() == "" || prenom!!.getText()
                        .toString() == "" || num_tlphn!!.getText()
                        .toString() == "" || email!!.getText()
                        .toString() == "" || mot_de_passe!!.getText().toString() == ""
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Vous devez remplir tous les champs",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    postData(
                        nom!!.getText().toString(),
                        prenom!!.getText().toString(),
                        num_tlphn!!.getText().toString(),
                        email!!.getText().toString(),
                        mot_de_passe!!.getText().toString()
                    )
                }
            }
        })

        var signUp_textView = view.findViewById(R.id.signUpTextView) as TextView
        signUp_textView.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            findNavController().navigate(action)
        }
    }

    private fun postData(
        nom: String,
        prenom: String,
        num_tlphn: String,
        email: String,
        mdp: String
    ) {
        loadingPB!!.visibility = View.VISIBLE
        val modal = compte(0, nom, prenom, num_tlphn, email, mdp, null, null)
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            requireActivity().runOnUiThread() {
                loadingPB!!.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
            }

        }
        loadingPB!!.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = Endpoint.createEndpoint().createAccount(modal)
            withContext(Dispatchers.Main) {
                loadingPB!!.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    if (response.body()== null) {
                        Toast.makeText(
                            requireContext(),
                            "Email existe déjà",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Compte créé",
                            Toast.LENGTH_SHORT
                        ).show()
<<<<<<< HEAD
                        saveConnexion(requireContext(), true, response.body()!!.nom,response.body()!!.prenom,response.body()!!.email,response.body()!!.numTelephone,response.body()!!.numCompte)
=======
                        saveConnexion(requireContext(), true, modal.nom, modal.prenom, modal.email, modal.numTelephone, modal.numCompte, modal.idCompte.toString())
>>>>>>> 0ce667afe51a10ff224998cc93a97efd22e9d4e1
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Une erreur s'est produite ******",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}