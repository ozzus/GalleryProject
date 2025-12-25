package com.example.galleryproject.session

import android.content.Context
import android.content.SharedPreferences
import com.example.galleryproject.models.Client
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveUser(client: Client) {
        val json = gson.toJson(client)
        prefs.edit().putString("current_user", json).apply()
    }

    fun getUser(): Client? {
        val json = prefs.getString("current_user", null)
        return gson.fromJson(json, Client::class.java)
    }

    fun clearUser() {
        prefs.edit().remove("current_user").apply()
    }

    fun saveToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("access_token").apply()
    }
}
