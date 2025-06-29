package com.example.galleryproject.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.galleryproject.R
import com.example.galleryproject.databinding.FragmentPhotoDetailBinding
import com.example.galleryproject.models.Photo

class PhotoDetailFragment : Fragment() {

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var photo: Photo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = PhotoDetailFragmentArgs.fromBundle(requireArguments()).photo
        bindPhotoData(photo)
        setupToolbar()
        setupNavigationResultListener()
    }

    private fun bindPhotoData(photo: Photo) {
        binding.imageView.load(photo.imageUrl)
        binding.imageName.text = photo.title
        binding.userName.text = photo.author
        binding.dateText.text = photo.date
        binding.description.text = photo.description
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            // Устанавливаем меню
            inflateMenu(R.menu.photo_detail_menu)

            // Обработка кликов по пунктам меню
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        navigateToEditScreen() // Исправлено: вызов перехода вместо диалога
                        true
                    }
                    else -> false
                }
            }

            // Обработка клика на кнопку "Назад"
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun navigateToEditScreen() {
        val direction = PhotoDetailFragmentDirections.actionPhotoDetailToEditPhoto(photo)
        findNavController().navigate(direction)
    }

    private fun setupNavigationResultListener() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Photo>("updated_photo")
            ?.observe(viewLifecycleOwner) { updatedPhoto ->
                photo = updatedPhoto
                bindPhotoData(updatedPhoto)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
