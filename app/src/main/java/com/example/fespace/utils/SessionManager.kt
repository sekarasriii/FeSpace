package com.example.fespace.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREF_NAME = "FeSpaceSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ROLE = "userRole"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_EMAIL = "userEmail"
    }
    
    /**
     * Save user session after successful login
     */
    fun saveSession(userId: Int, role: String, name: String, email: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_ROLE, role)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Get current user ID
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }
    
    /**
     * Get current user role
     */
    fun getUserRole(): String? {
        return prefs.getString(KEY_USER_ROLE, null)
    }
    
    /**
     * Get current user name
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Get current user email
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Clear session on logout
     */
    fun clearSession() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }
    
    /**
     * Get all session data as a data class
     */
    fun getSessionData(): SessionData? {
        return if (isLoggedIn()) {
            SessionData(
                userId = getUserId(),
                role = getUserRole() ?: "",
                name = getUserName() ?: "",
                email = getUserEmail() ?: ""
            )
        } else {
            null
        }
    }
}

/**
 * Data class to hold session information
 */
data class SessionData(
    val userId: Int,
    val role: String,
    val name: String,
    val email: String
)
