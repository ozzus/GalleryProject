package com.example.galleryproject.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    var username: String = "",
    var birthday: String = "",
    var email: String = "",
    var phone: String = "",
    var avatarUrl: String? = null
) : Parcelable