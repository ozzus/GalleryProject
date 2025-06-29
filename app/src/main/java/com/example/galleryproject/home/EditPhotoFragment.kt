package com.example.galleryproject.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentEditPhotoBinding
import com.example.galleryproject.models.Photo

class EditPhotoFragment : Fragment(R.layout.fragment_edit_photo) {

    private var _binding: FragmentEditPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var photo: Photo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditPhotoBinding.bind(view)

        // Получаем фото из аргументов
        photo = EditPhotoFragmentArgs.fromBundle(requireArguments()).photo

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // Заполняем поля данными
        binding.editTitle.setText(photo.title)
        binding.editDescription.setText(photo.description)

        // Устанавливаем статус
        if (photo.isPopular) {
            binding.radioPopular.isChecked = true
        } else {
            binding.radioNew.isChecked = true
        }

        // Настройка Toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            // Обновляем данные фото
            val updatedPhoto = photo.copy(
                title = binding.editTitle.text.toString(),
                description = binding.editDescription.text.toString(),
                isPopular = binding.radioPopular.isChecked
            )

            // Возвращаем результат
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("updated_photo", updatedPhoto)

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}