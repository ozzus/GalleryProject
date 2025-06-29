package com.example.galleryproject.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val author: String,
    val date: String,
    val description: String,
    val isPopular: Boolean = false
) : Parcelable