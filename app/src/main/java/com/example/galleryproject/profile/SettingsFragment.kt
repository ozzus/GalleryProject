package com.example.galleryproject.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentSettingsBinding
import com.example.galleryproject.models.Client
import com.example.galleryproject.session.UserSession
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSession: UserSession
    private lateinit var currentUser: Client

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        userSession = UserSession(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Загружаем данные пользователя
        currentUser = userSession.getUser() ?: run {
            // Если пользователя нет, переходим на логин
            navigateToLogin()
            return
        }

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        with(binding) {
            // Настройка Toolbar
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            // Устанавливаем email в заголовок
            toolbar.title = currentUser.email

            // Заполнение данных пользователя
            editUsername.setText(currentUser.username)
            editBirthday.setText(currentUser.birthday)
            editEmail.setText(currentUser.email)
            editPhone.setText(currentUser.phone)
        }
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            saveUserData()
        }

        binding.btnDeleteAccount.setOnClickListener {
            showDeleteAccountConfirmation()
        }

        binding.btnSignOut.setOnClickListener {
            signOutUser()
        }
    }

    private fun saveUserData() {
        val newUsername = binding.editUsername.text.toString()
        val newBirthday = binding.editBirthday.text.toString()
        val newEmail = binding.editEmail.text.toString()
        val newPhone = binding.editPhone.text.toString()

        // Проверка, были ли изменения
        if (newUsername == currentUser.username &&
            newBirthday == currentUser.birthday &&
            newEmail == currentUser.email &&
            newPhone == currentUser.phone) {
            Snackbar.make(binding.root, "No changes detected", Snackbar.LENGTH_SHORT).show()
            return
        }

        val updatedUser = currentUser.copy(
            username = newUsername,
            birthday = newBirthday,
            email = newEmail,
            phone = newPhone
        )

        // Сохраняем обновленные данные
        userSession.saveUser(updatedUser)
        currentUser = updatedUser

        // Уведомляем об успехе
        Snackbar.make(binding.root, "Profile updated", Snackbar.LENGTH_SHORT).show()

        // Возвращаем обновленные данные в профиль
        findNavController().previousBackStackEntry?.savedStateHandle?.set("updated_user", updatedUser)
    }

    private fun showDeleteAccountConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount() {
        // Реализация удаления аккаунта
        userSession.clearUser()
        Snackbar.make(binding.root, "Account deleted", Snackbar.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun signOutUser() {
        // Очищаем данные пользователя
        userSession.clearUser()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_global_login)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}