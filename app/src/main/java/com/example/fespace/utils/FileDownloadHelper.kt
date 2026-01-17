package com.example.fespace.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object FileDownloadHelper {
    
    /**
     * Open/view a file using system default app
     * @param context Android context
     * @param filePath Absolute path to the file
     */
    fun openFile(context: Context, filePath: String?) {
        if (filePath == null) {
            Toast.makeText(context, "File tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }
        
        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(context, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }
        
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val mimeType = when (file.extension.lowercase()) {
                "pdf" -> "application/pdf"
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                else -> "*/*"
            }
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            
            context.startActivity(Intent.createChooser(intent, "Buka dengan"))
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal membuka file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    /**
     * Share a file using system share dialog
     * @param context Android context
     * @param filePath Absolute path to the file
     */
    fun shareFile(context: Context, filePath: String?) {
        if (filePath == null) {
            Toast.makeText(context, "File tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }
        
        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(context, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }
        
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val mimeType = when (file.extension.lowercase()) {
                "pdf" -> "application/pdf"
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                else -> "*/*"
            }
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            
            context.startActivity(Intent.createChooser(intent, "Bagikan dokumen"))
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal membagikan file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    /**
     * Get file size in human-readable format
     */
    fun getFileSize(filePath: String?): String {
        if (filePath == null) return "0 KB"
        
        val file = File(filePath)
        if (!file.exists()) return "0 KB"
        
        val bytes = file.length()
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> "${bytes / (1024 * 1024)} MB"
        }
    }
}
