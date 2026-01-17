package com.example.fespace.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility object for date formatting
 */
object DateUtils {
    
    /**
     * Format timestamp to readable date string
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted date string (e.g., "11 Jan 2026, 14:30")
     */
    fun formatDate(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }
    
    /**
     * Format timestamp to date only (without time)
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted date string (e.g., "11 Januari 2026")
     */
    fun formatDateOnly(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }
    
    /**
     * Format timestamp to time only
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted time string (e.g., "14:30")
     */
    fun formatTimeOnly(timestamp: Long): String {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale("id", "ID"))
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }
}
