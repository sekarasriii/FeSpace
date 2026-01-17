package com.example.fespace.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

object ImageUploadHelper {
    
    /**
     * Copy image from URI to internal storage
     * Returns the absolute path of the saved file
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri, prefix: String = "img"): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            
            // Get original filename
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            val fileName = cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) it.getString(nameIndex) else null
                } else null
            } ?: "${prefix}_${System.currentTimeMillis()}.jpg"
            
            // Create file in internal storage
            val file = File(context.filesDir, "images/$fileName")
            file.parentFile?.mkdirs()
            
            // Copy file
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Get file size from URI in bytes
     */
    fun getFileSize(context: Context, uri: Uri): Long {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex >= 0) it.getLong(sizeIndex) else 0L
                } else 0L
            } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    
    /**
     * Get filename from URI
     */
    fun getFileName(context: Context, uri: Uri): String {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) it.getString(nameIndex) else "unknown"
                } else "unknown"
            } ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }
    
    /**
     * Delete image file
     */
    fun deleteImage(imagePath: String?): Boolean {
        return try {
            if (imagePath != null) {
                val file = File(imagePath)
                if (file.exists()) {
                    file.delete()
                } else false
            } else false
        } catch (e: Exception) {
            false
        }
    }
}
