package com.example.fespace.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Utility for formatting and parsing Indonesian Rupiah currency
 */
object RupiahFormatter {
    
    /**
     * Format double amount to Rupiah string with thousand separators (dots)
     * @param amount The amount to format
     * @return Formatted string like "Rp100.000" or "Rp1.000.000"
     */
    fun formatToRupiah(amount: Double): String {
        val symbols = DecimalFormatSymbols(Locale("id", "ID"))
        symbols.groupingSeparator = '.'
        symbols.decimalSeparator = ','
        
        val formatter = DecimalFormat("#,###", symbols)
        return "Rp${formatter.format(amount)}"
    }
    
    /**
     * Parse Rupiah formatted string to double
     * @param formatted String like "Rp100.000" or "100.000"
     * @return Double value like 100000.0
     */
    fun parseFromRupiah(formatted: String): Double {
        // Remove "Rp", spaces, and dots
        val cleaned = formatted
            .replace("Rp", "")
            .replace(" ", "")
            .replace(".", "")
            .replace(",", ".")
            .trim()
        
        return cleaned.toDoubleOrNull() ?: 0.0
    }
    
    /**
     * Format input string with thousand separators as user types
     * @param input Digits-only string like "100000"
     * @return Formatted string like "100.000"
     */
    fun formatInputWithDots(input: String): String {
        if (input.isEmpty()) return ""
        
        // Remove any existing dots
        val digitsOnly = input.replace(".", "")
        
        // Add dots every 3 digits from right
        val reversed = digitsOnly.reversed()
        val formatted = StringBuilder()
        
        reversed.forEachIndexed { index, char ->
            if (index > 0 && index % 3 == 0) {
                formatted.append('.')
            }
            formatted.append(char)
        }
        
        return formatted.reverse().toString()
    }
    
    /**
     * Validate price value
     * @param price Price to validate
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validatePrice(price: Double): Pair<Boolean, String> {
        if (price <= 0) {
            return Pair(false, "Harga harus lebih dari 0")
        }
        
        if (price < 10000) {
            return Pair(false, "Harga minimal Rp10.000")
        }
        
        if (price > 1000000000) {
            return Pair(false, "Harga maksimal Rp1.000.000.000")
        }
        
        return Pair(true, "")
    }
}
