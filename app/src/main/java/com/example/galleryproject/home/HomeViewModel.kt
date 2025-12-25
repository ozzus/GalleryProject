package com.example.galleryproject.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galleryproject.data.photos.PhotoRepository
import com.example.galleryproject.models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    photoRepository: PhotoRepository
) : ViewModel() {
    private val _photos = MutableLiveData(photoRepository.getPhotos())
    val photos: LiveData<List<Photo>> = _photos
}
