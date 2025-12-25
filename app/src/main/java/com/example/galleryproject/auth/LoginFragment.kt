package com.example.galleryproject.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentLoginBinding
import com.example.galleryproject.ui.state.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            binding.tilEmail.error = null
            binding.tilPassword.error = null

            if (!isValidEmail(email)) {
                binding.tilEmail.error = "Invalid email format"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                binding.tilPassword.error = "Password is required"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.btnGuest.setOnClickListener {
            viewModel.loginAsGuest()
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> {
                    showLoading(false)
                    navigateToProfile()
                }
                is UiState.Error -> {
                    showLoading(false)
                    showError("Login failed: ${state.message}")
                }
                UiState.Idle -> showLoading(false)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModel.login(email, password)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnLogin.isEnabled = !isLoading
        binding.btnRegister.isEnabled = !isLoading
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
