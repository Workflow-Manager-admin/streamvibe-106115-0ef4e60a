package com.example.streamvibe.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.streamvibe.model.User
import java.util.UUID

// PUBLIC_INTERFACE
object AuthRepository {
    private const val PREF_NAME = "streamvibe_user_prefs"
    private const val KEY_USER_ID = "user_id"
    var currentUser: User? = null
        private set

    private val users = mutableListOf<User>(
        User(
            id = "1",
            email = "demo@streamvibe.com",
            name = "Demo User",
            password = "password",
            avatarUrl = null
        )
    )

    fun isLoggedIn(context: Context): Boolean {
        if (currentUser != null) return true
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getString(KEY_USER_ID, null)
        val user = users.find { it.id == userId }
        if (user != null) {
            currentUser = user
            return true
        }
        return false
    }

    fun login(context: Context, email: String, password: String): Boolean {
        val user = users.find { it.email == email && it.password == password }
        if (user != null) {
            currentUser = user
            saveSession(context, user.id)
            return true
        }
        return false
    }

    fun signUp(context: Context, name: String, email: String, password: String): Boolean {
        if (users.any { it.email == email }) return false
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            password = password
        )
        users.add(user)
        currentUser = user
        saveSession(context, user.id)
        return true
    }

    fun logout(context: Context) {
        currentUser = null
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    fun getCurrentUser(): User? = currentUser

    fun updateProfile(name: String, email: String, avatarUrl: String?) {
        currentUser?.let {
            it.name = name
            it.email = email
            it.avatarUrl = avatarUrl
        }
    }

    private fun saveSession(context: Context, userId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }
}
