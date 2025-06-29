package com.example.galleryproject.auth

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("token")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("users")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}


    data class LoginRequest(
        val grant_type: String = "password",
        val username: String,
        val password: String,
        val client_id: String = "your_client_id",
        val client_secret: String = "your_client_secret"
    )


    data class RegisterRequest(
        val email: String,
        val birthday: String,

        @SerializedName("displayName")
        val username: String,

        val phone: String? = null,
        val plainPassword: String
    )


    data class AuthResponse(
        @SerializedName("access_token")
        val accessToken: String,

        @SerializedName("expires_in")
        val expiresIn: Int,

        @SerializedName("token_type")
        val tokenType: String
    )


    data class RegisterResponse(
        val id: Int,
        val email: String,

        @SerializedName("displayName")
        val username: String
    )





