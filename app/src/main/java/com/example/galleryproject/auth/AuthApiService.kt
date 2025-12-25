package com.example.galleryproject.auth

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("token")
    suspend fun login(@FieldMap fields: Map<String, String>): AuthResponse

    @Headers(
        "Content-Type: application/ld+json",
        "Accept: application/ld+json"
    )
    @POST("users")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}


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





