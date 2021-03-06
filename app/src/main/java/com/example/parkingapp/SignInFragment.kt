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
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Google sign in handling
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        var signIn_textView = view.findViewById(R.id.signInTextView) as TextView
        signIn_textView.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        var googleSinginButton = view.findViewById(R.id.sign_in_button) as SignInButton
        googleSinginButton.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        };

        // When clicking on sign-in button
        view.findViewById<Button>(R.id.login).setOnClickListener {

            var email: String = view.findViewById<TextView>(R.id.email).text.toString()
            var pwd: String =view.findViewById<TextView>(R.id.password).text.toString()

            // Inserting Coroutine
            val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
                val text = "Probl??me de connextion"
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
                            val toast = Toast.makeText(context, "Vous ??tes connect??" + data.idCompte.toString(), duration)
                            toast.show()

                            saveConnexion(requireContext(), true, data.nom, data.prenom, data.email, data.numTelephone, data.numCompte, data.idCompte.toString())
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            val text = "Mot de Passe erron??"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
>>>>>>> 0ce667afe51a10ff224998cc93a97efd22e9d4e1
        }


        var googleSinginButton = view.findViewById(R.id.sign_in_button) as SignInButton
        googleSinginButton.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        };

        // When clicking on sign-in button
        view.findViewById<Button>(R.id.login).setOnClickListener {

            var email: String = view.findViewById<TextView>(R.id.email).text.toString()
            var pwd: String =view.findViewById<TextView>(R.id.password).text.toString()

            if(email.isNotEmpty() || pwd.isNotEmpty()){
                // Inserting Coroutine
                val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
                    val text = "Probl??me de connextion"
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
                                val toast = Toast.makeText(context, "Vous ??tes connect??", duration)
                                toast.show()

                                saveConnexion(requireContext(), true, data.nom, data.prenom, data.email, data.numTelephone, data.numCompte)
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            } else {
                                val text = "Mot de Passe erron??"
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
            }else{
                Toast.makeText(
                    requireContext(),
                    "Vous devez remplir tous les champs",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            saveConnexion(requireContext(), true, account.familyName, account.givenName, account.email, "", "")
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            val text = "Erreur Serveur : " + GoogleSignInStatusCodes.getStatusCodeString(e.statusCode)
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()

        }

    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            saveConnexion(requireContext(), true, account.familyName, account.givenName, account.email, "", "", account.id)
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            val text = "Erreur Serveur : " + GoogleSignInStatusCodes.getStatusCodeString(e.statusCode)
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()

        }

    }
}