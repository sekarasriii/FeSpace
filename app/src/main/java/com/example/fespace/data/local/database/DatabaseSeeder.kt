package com.example.fespace.data.local.database

import android.content.Context
import com.example.fespace.data.local.entity.UserEntity
import com.example.fespace.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Database seeder to populate initial data
 */
object DatabaseSeeder {
    
    private const val PREFS_NAME = "fespace_seeder_prefs"
    private const val KEY_SEEDED = "database_seeded"
    
    /**
     * Seed database with initial admin account
     * Only runs once per app installation
     */
    fun seedDatabase(context: Context, userRepository: UserRepository) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val alreadySeeded = prefs.getBoolean(KEY_SEEDED, false)
        
        if (alreadySeeded) {
            return // Already seeded, skip
        }
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Create default admin account
                val adminUser = UserEntity(
                    nameUser = "rahayu",
                    email = "rahayu@gmail.com",
                    password = "@Rahayu123",
                    role = "admin",
                    whatsappNumber = "+6281212345678"
                )
                
                userRepository.insertUser(adminUser)
                
                // Mark as seeded
                prefs.edit().putBoolean(KEY_SEEDED, true).apply()
                
                android.util.Log.d("DatabaseSeeder", "Admin account created successfully")
            } catch (e: Exception) {
                android.util.Log.e("DatabaseSeeder", "Error seeding database", e)
            }
        }
    }
    
    /**
     * Reset seeder flag (for testing purposes)
     */
    fun resetSeeder(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SEEDED, false).apply()
    }
}
