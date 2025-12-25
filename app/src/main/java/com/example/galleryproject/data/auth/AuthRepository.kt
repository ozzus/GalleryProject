package com.example.galleryproject.data.auth

import com.example.galleryproject.auth.AuthApiService
import com.example.galleryproject.auth.AuthResponse
import com.example.galleryproject.auth.RegisterRequest
import com.example.galleryproject.auth.RegisterResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun login(email: String, password: String): AuthResponse {
        val fields = mapOf(
            "grant_type" to "password",
            "username" to email,
            "password" to password
        )
        return authApiService.login(fields)
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return authApiService.register(request)
    }
}
