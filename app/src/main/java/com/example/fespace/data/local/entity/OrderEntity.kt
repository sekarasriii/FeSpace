package com.example.fespace.data.local.entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val idOrder: Int = 0,
    val idClient: Int,
    val idAdmin: Int,
    val idService: Int,
    val locationAddress: String,
    val budget: Double,
    // pending, approved, survey, design, revision, final, completed
    val status: String,
    val createAt: Long,
    val updateAt: Long
)
