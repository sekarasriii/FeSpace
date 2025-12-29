package com.example.fespace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "order_documents")
data class OrderDocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val idDocuments: Int = 0, // Mengikuti penamaan id_documents

    @ColumnInfo(name = "id_orders")
    val idOrders: Int,

    @ColumnInfo(name = "uploaded_by")
    val uploadedBy: Int, // id_user pengunggah
    val filePath: String,
    val fileName: String,
    // ENUM di SQL jadi String di Room: 'location_photo', 'client_document', dll
    val docType: String,
    val description: String?,

    @ColumnInfo(name = "uploaded_at")
    val uploadAt: Long = System.currentTimeMillis()
)

