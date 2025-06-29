package com.example.galleryproject.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryproject.databinding.FragmentHomeBinding
import com.example.galleryproject.models.Photo
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val photos = listOf(
        Photo(
            id = 1,
            imageUrl = "https://picsum.photos/id/10/300/300",
            title = "House View",
            author = "John Doe",
            date = "23.08.2022",
            description = "A cozy house in Italy...",
            isPopular = false
        ),
        Photo(
            id = 2,
            imageUrl = "https://picsum.photos/id/20/300/300",
            title = "Lake",
            author = "Alice Smith",
            date = "12.06.2022",
            description = "Peaceful morning on the lake.",
            isPopular = false
        ),
        Photo(
            id = 3,
            imageUrl = "https://picsum.photos/id/30/300/300",
            title = "Mountain",
            author = "Bob Ross",
            date = "10.04.2022",
            description = "A calm mountain view.",
            isPopular = true
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PhotoAdapter(photos) { photo ->
            val action = HomeFragmentDirections.actionHomeToPhotoDetail(photo)
            findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}