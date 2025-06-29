package com.example.galleryproject.models

data class RegisterRequest(
    val username: String,
    val birthday: String,
    val email: String,
    val phone: String,
    val password: String,
    val confirmPassword: String
)