package com.example.galleryproject.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentRegisterBinding
import com.example.galleryproject.ui.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etBirthday.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                registerUser()
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }

        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> {
                    showLoading(false)
                    showSuccessAndNavigateToLogin()
                }
                is UiState.Error -> {
                    showLoading(false)
                    showError("Registration failed: ${state.message}")
                }
                UiState.Idle -> showLoading(false)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _: DatePicker, year: Int, month: Int, day: Int ->
            binding.etBirthday.setText("$day/${month + 1}/$year")
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateForm(): Boolean {
        var isValid = true
        val required = "This field is required"

        with(binding) {
            tilUsername.error = null
            tilBirthday.error = null
            tilEmail.error = null
            tilPassword.error = null
            tilConfirmPassword.error = null

            if (etUsername.text.isNullOrEmpty()) {
                tilUsername.error = required
                isValid = false
            }

            if (etBirthday.text.isNullOrEmpty()) {
                tilBirthday.error = required
                isValid = false
            } else if (!isValidBirthday(etBirthday.text.toString())) {
                tilBirthday.error = "Invalid date format (dd/MM/yyyy)"
                isValid = false
            }

            if (etEmail.text.isNullOrEmpty()) {
                tilEmail.error = required
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
                tilEmail.error = "Invalid email format"
                isValid = false
            }

            if (etPassword.text.isNullOrEmpty()) {
                tilPassword.error = required
                isValid = false
            } else if (etPassword.text.toString().length < 6) {
                tilPassword.error = "Password must be at least 6 characters"
                isValid = false
            }

            if (etConfirmPassword.text.isNullOrEmpty()) {
                tilConfirmPassword.error = required
                isValid = false
            } else if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                tilConfirmPassword.error = "Passwords don't match"
                isValid = false
            }
        }

        return isValid
    }

    private fun isValidBirthday(date: String): Boolean {
        return try {
            val parts = date.split("/")
            parts.size == 3 &&
                    parts[0].toIntOrNull() != null &&
                    parts[1].toIntOrNull() != null &&
                    parts[2].toIntOrNull() != null
        } catch (e: Exception) {
            false
        }
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val birthday = convertDateToApiFormat(binding.etBirthday.text.toString())
        val phone = binding.etPhone.text?.toString()?.trim()

        viewModel.register(
            RegisterRequest(
                email = email,
                birthday = birthday,
                username = username,
                phone = phone,
                plainPassword = password
            )
        )
    }

    private fun convertDateToApiFormat(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            outputFormat.format(inputFormat.parse(date)!!)
        } catch (e: Exception) {

            "2000-01-01T00:00:00.000"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnRegister.isEnabled = !isLoading
        binding.btnLogin.isEnabled = !isLoading
    }

    private fun showSuccessAndNavigateToLogin() {
        android.widget.Toast.makeText(
            requireContext(),
            "Registration successful!",
            android.widget.Toast.LENGTH_SHORT
        ).show()

        // Переходим на экран логина через 1.5 секунды
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_register_to_login)
        }, 1500)
    }

    private fun showError(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
