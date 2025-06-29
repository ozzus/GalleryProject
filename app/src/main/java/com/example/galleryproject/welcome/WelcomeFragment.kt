package com.example.galleryproject.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galleryproject.R
import com.google.android.material.button.MaterialButton

class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_login)
        }

        view.findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_register)
        }
    }
}