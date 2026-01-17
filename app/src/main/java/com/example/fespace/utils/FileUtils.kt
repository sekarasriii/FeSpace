package com.example.fespace.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Utility object for handling file storage (images and PDFs)
 * Supports JPG, PNG, and PDF with 10MB size limit
 */
object FileUtils {
    
    private const val MAX_FILE_SIZE_MB = 10
    private const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024 // 10MB in bytes
    
    /**
     * Save file (image or PDF) to internal storage
     * Validates file size (max 10MB) and type
     * @return Absolute file path or null if failed/invalid
     */
    fun saveFileToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            // Get file size
            val fileSize = context.contentResolver.openInputStream(uri)?.use { input ->
                input.available().toLong()
            } ?: 0L
            
            // Validate file size
            if (fileSize > MAX_FILE_SIZE_BYTES) {
                return null // File too large
            }
            
            // Get MIME type and determine extension
            val mimeType = context.contentResolver.getType(uri)
            val extension = when {
                mimeType?.contains("pdf") == true -> "pdf"
                mimeType?.contains("png") == true -> "png"
                mimeType?.contains("jpeg") == true || mimeType?.contains("jpg") == true -> "jpg"
                else -> return null // Unsupported file type
            }
            
            val fileName = "file_${System.currentTimeMillis()}.$extension"
            val file = File(context.filesDir, fileName)
            
            // Copy file directly to internal storage
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Get file extension from path
     */
    fun getFileExtension(path: String?): String? {
        return path?.substringAfterLast('.', "")?.lowercase()
    }
    
    /**
     * Check if file is PDF
     */
    fun isPdf(path: String?): Boolean {
        return getFileExtension(path) == "pdf"
    }
    
    /**
     * Check if file is image (JPG or PNG)
     */
    fun isImage(path: String?): Boolean {
        val ext = getFileExtension(path)
        return ext == "jpg" || ext == "jpeg" || ext == "png"
    }
    
    /**
     * Get file size in MB from URI
     */
    fun getFileSizeMB(context: Context, uri: Uri): Double {
        return try {
            val sizeBytes = context.contentResolver.openInputStream(uri)?.use { input ->
                input.available().toLong()
            } ?: 0L
            sizeBytes / (1024.0 * 1024.0)
        } catch (e: Exception) {
            0.0
        }
    }
    
    /**
     * Delete file from internal storage
     */
    fun deleteFile(path: String?): Boolean {
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
    
    /**
     * Open file with external app (PDF reader, image viewer, etc.)
     * Uses FileProvider for secure file sharing
     */
    fun openFile(context: Context, filePath: String) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                android.widget.Toast.makeText(context, "File tidak ditemukan", android.widget.Toast.LENGTH_SHORT).show()
                return
            }
            
            // Create content URI using FileProvider
            val uri = androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            // Determine MIME type
            val mimeType = when (getFileExtension(filePath)) {
                "pdf" -> "application/pdf"
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                else -> "*/*"
            }
            
            // Create Intent to view file
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            
            context.startActivity(android.content.Intent.createChooser(intent, "Buka dengan"))
        } catch (e: Exception) {
            android.widget.Toast.makeText(context, "Tidak ada aplikasi untuk membuka file ini", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
