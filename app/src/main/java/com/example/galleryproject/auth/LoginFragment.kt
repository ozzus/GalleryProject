package com.example.galleryproject.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentLoginBinding
import com.example.galleryproject.models.Client
import com.example.galleryproject.session.UserSession
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSession: UserSession

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        userSession = UserSession(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (isValidEmail(email)) {
                loginUser(email, password)
            } else {
                binding.tilEmail.error = "Invalid email format"
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun loginUser(email: String, password: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val client = Client(
                    username = "Demo User",
                    birthday = "01/01/1990",
                    email = email,
                    phone = "+7(999)123-45-67"
                )

                // Сохраняем пользователя
                userSession.saveUser(client)
            } catch (e: Exception) {
                showError("Login failed: ${e.message ?: "Unknown error"}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnLogin.isEnabled = !isLoading
        binding.btnRegister.isEnabled = !isLoading
    }

    private fun saveAuthToken(token: String) {
        val sharedPref = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("access_token", token).apply()
    }

    private fun navigateToProfile() {
        findNavController().navigate(R.id.action_login_to_home)
    }

    private fun showError(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}