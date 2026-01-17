package com.example.fespace.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream

/**
 * Utility object for handling image storage and retrieval
 * Uses internal storage for simplicity (no permissions needed)
 */
object ImageUtils {
    
    /**
     * Save image from URI to internal storage
     * Compresses image to reduce file size
     * @return Absolute file path of saved image
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            
            // Open input stream from URI
            context.contentResolver.openInputStream(uri)?.use { input ->
                // Decode and compress bitmap
                val bitmap = BitmapFactory.decodeStream(input)
                
                // Compress to reduce file size (quality 80%)
                FileOutputStream(file).use { output ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
                }
            }
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Load image from file path as ImageBitmap for Compose
     * @return ImageBitmap or null if file doesn't exist
     */
    fun loadImageFromPath(path: String?): ImageBitmap? {
        return try {
            path?.let {
                val file = File(it)
                if (file.exists()) {
                    BitmapFactory.decodeFile(it)?.asImageBitmap()
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Delete image file from internal storage
     * @return true if deleted successfully
     */
    fun deleteImage(path: String?): Boolean {
        return try {
            path?.let {
                val file = File(it)
                file.delete()
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
