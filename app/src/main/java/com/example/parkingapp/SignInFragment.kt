package com.example.parkingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var signUp_textView = view.findViewById(R.id.signInTextView) as TextView
        signUp_textView.setOnClickListener{
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }
}