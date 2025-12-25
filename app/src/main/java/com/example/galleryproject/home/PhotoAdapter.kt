package com.example.galleryproject.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.galleryproject.R
import com.example.galleryproject.databinding.ItemPhotoBinding
import com.example.galleryproject.models.Photo

class PhotoAdapter(
    initialPhotos: List<Photo>,
    private val onItemClick: (Photo) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photos: List<Photo> = initialPhotos

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.imageView.load(photo.imageUrl) {
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_placeholder)
            }

            binding.root.setOnClickListener {
                onItemClick(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size

    fun updatePhotos(newPhotos: List<Photo>) {
        photos = newPhotos
        notifyDataSetChanged()
    }
}
