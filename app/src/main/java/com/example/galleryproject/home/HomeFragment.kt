package com.example.galleryproject.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryproject.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.galleryproject.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PhotoAdapter(emptyList()) { photo ->
            val navController = findNavController()
            val actionId = if (navController.currentDestination?.id == R.id.photosFragment) {
                R.id.action_photos_to_photo_detail
            } else {
                R.id.action_home_to_photo_detail
            }
            navController.navigate(actionId, bundleOf("photo" to photo))
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            adapter.updatePhotos(photos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
