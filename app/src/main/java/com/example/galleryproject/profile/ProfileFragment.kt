package com.example.galleryproject.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentProfileBinding
import com.example.galleryproject.models.Client
import com.example.galleryproject.session.UserSession
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userSession: UserSession
    private lateinit var currentUser: Client

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                updateProfileImage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userSession = UserSession(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Загружаем данные пользователя
        currentUser = userSession.getUser() ?: run {
            // Если пользователя нет, возвращаемся назад
            findNavController().navigateUp()
            return
        }

        // Отображаем данные
        bindUserData(currentUser)

        // Слушатель для обновлений из SettingsFragment
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Client>("updated_user")
            ?.observe(viewLifecycleOwner) { updatedUser ->
                currentUser = updatedUser
                bindUserData(updatedUser)
            }

        setupListeners()
    }

    private fun bindUserData(user: Client) {
        with(binding) {
            usernameTextView.text = user.username
            birthdayTextView.text = user.birthday

            if (!user.avatarUrl.isNullOrEmpty()) {
                val uri = Uri.parse(user.avatarUrl)
                profileImage.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.web_ant_logo)
                    error(R.drawable.web_ant_logo)
                }
            } else {
                profileImage.load(R.drawable.web_ant_logo) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    private fun setupListeners() {
        binding.settingsIcon.setOnClickListener {
            navigateToSettings()
        }

        binding.profileImage.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.action_profile_to_settings)
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Gallery", "Google Drive", "Google Photos")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Change profile picture")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openGoogleDrive()
                    2 -> openGooglePhotos()
                }
            }
            .show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun openGoogleDrive() {
        Snackbar.make(binding.root, "Google Drive not implemented", Snackbar.LENGTH_SHORT).show()
    }

    private fun openGooglePhotos() {
        // Заглушка для Google Photos
        Snackbar.make(binding.root, "Google Photos not implemented", Snackbar.LENGTH_SHORT).show()
    }

    private fun updateProfileImage(uri: Uri) {
        val updatedUser = currentUser.copy(avatarUrl = uri.toString())
        userSession.saveUser(updatedUser)
        currentUser = updatedUser

        binding.profileImage.load(uri) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }

        Snackbar.make(binding.root, "Profile image updated", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}