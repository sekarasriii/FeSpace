package com.example.fespace.data.local.entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val idUser: Int = 0,
    val nameUser: String,
    val email: String,
    val password: String,
    val role: String,
    val whatsappNumber: String? = null,
    val createAt: Long = System.currentTimeMillis(),
    val updateAt: Long = System.currentTimeMillis()
)
