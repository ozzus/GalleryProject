package com.example.galleryproject.session

import android.content.Context
import android.content.SharedPreferences
import com.example.galleryproject.models.Client
import com.google.gson.Gson

class UserSession(context: Context) {

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
}