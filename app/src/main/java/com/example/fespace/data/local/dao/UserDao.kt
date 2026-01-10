package com.example.fespace.data.local.dao

import androidx.room.*
import com.example.fespace.data.local.entity.UserEntity

@Dao
interface UserDao {
    // ... other methods ...

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    // Make sure these also exist as your Repository calls them:
    @Query("SELECT * FROM users WHERE email = :email AND password = :pass LIMIT 1")
    suspend fun getUser(email: String, pass: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE idUser = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    fun getClientCount(): kotlinx.coroutines.flow.Flow<Int>
}
