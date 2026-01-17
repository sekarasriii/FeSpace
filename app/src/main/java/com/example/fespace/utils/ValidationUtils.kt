package com.example.fespace.utils

import java.util.regex.Pattern

object ValidationUtils {
    
    // User Validation
    fun validateName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Nama tidak boleh kosong")
        }
        // Alphabet, spaces, dots, commas only
        val namePattern = "^[a-zA-Z\\s.,]+$"
        if (!name.matches(Regex(namePattern))) {
            return ValidationResult(false, "Nama hanya boleh berisi huruf, spasi, titik, dan koma")
        }
        return ValidationResult(true, "")
    }
    
    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(false, "Email tidak boleh kosong")
        }
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        if (!email.matches(Regex(emailPattern))) {
            return ValidationResult(false, "Format email tidak valid")
        }
        return ValidationResult(true, "")
    }
    
    fun validateWhatsApp(number: String): ValidationResult {
        if (number.isBlank()) {
            return ValidationResult(false, "Nomor WhatsApp tidak boleh kosong")
        }
        // Must start with +62
        if (!number.startsWith("+62")) {
            return ValidationResult(false, "Nomor harus diawali dengan +62")
        }
        // Remove +62 and check length (10-15 digits)
        val digitsOnly = number.substring(3).filter { it.isDigit() }
        if (digitsOnly.length !in 10..15) {
            return ValidationResult(false, "Nomor harus 10-15 digit setelah +62")
        }
        return ValidationResult(true, "")
    }
    
    fun validatePassword(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(false, "Password minimal 8 karakter")
        }
        return ValidationResult(true, "")
    }
    
    // Portfolio Validation
    fun validatePortfolioTitle(title: String): ValidationResult {
        if (title.isBlank()) {
            return ValidationResult(false, "Judul tidak boleh kosong")
        }
        if (title.length < 5) {
            return ValidationResult(false, "Judul minimal 5 karakter")
        }
        if (title.length > 200) {
            return ValidationResult(false, "Judul maksimal 200 karakter")
        }
        if (title.matches(Regex("^[0-9\\s.]+$"))) {
            return ValidationResult(false, "Judul tidak boleh hanya berisi angka")
        }
        return ValidationResult(true, "")
    }
    
    fun validateDescription(description: String): ValidationResult {
        if (description.isBlank()) {
            return ValidationResult(false, "Deskripsi tidak boleh kosong")
        }
        if (description.length < 20) {
            return ValidationResult(false, "Deskripsi minimal 20 karakter")
        }
        return ValidationResult(true, "")
    }
    
    fun validateYear(year: String): ValidationResult {
        val yearInt = year.toIntOrNull()
        if (yearInt == null) {
            return ValidationResult(false, "Tahun harus berupa angka")
        }
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        if (yearInt !in 1900..currentYear) {
            return ValidationResult(false, "Tahun harus antara 1900-$currentYear")
        }
        return ValidationResult(true, "")
    }
    
    // Service Validation
    fun validateServiceName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "Nama layanan tidak boleh kosong")
        }
        if (name.length > 100) {
            return ValidationResult(false, "Nama layanan maksimal 100 karakter")
        }
        if (name.matches(Regex("^[0-9\\s.]+$"))) {
            return ValidationResult(false, "Nama layanan tidak boleh hanya berisi angka")
        }
        return ValidationResult(true, "")
    }
    
    fun validatePrice(price: String): ValidationResult {
        val priceDouble = price.toDoubleOrNull()
        if (priceDouble == null) {
            return ValidationResult(false, "Harga harus berupa angka")
        }
        if (priceDouble <= 0) {
            return ValidationResult(false, "Harga harus lebih dari 0")
        }
        return ValidationResult(true, "")
    }
    
    fun validateDuration(duration: String): ValidationResult {
        if (duration.isBlank()) {
            return ValidationResult(false, "Durasi tidak boleh kosong")
        }
        return ValidationResult(true, "")
    }
    
    // Order Validation
    fun validateAddress(address: String): ValidationResult {
        if (address.isBlank()) {
            return ValidationResult(false, "Alamat tidak boleh kosong")
        }
        if (address.length < 10) {
            return ValidationResult(false, "Alamat minimal 10 karakter")
        }
        return ValidationResult(true, "")
    }
    
    fun validateBudget(budget: String): ValidationResult {
        val budgetDouble = budget.toDoubleOrNull()
        if (budgetDouble == null) {
            return ValidationResult(false, "Budget harus berupa angka")
        }
        if (budgetDouble <= 0) {
            return ValidationResult(false, "Budget harus lebih dari 0")
        }
        return ValidationResult(true, "")
    }
    
    // File Validation
    fun validateFileSize(sizeInBytes: Long): ValidationResult {
        val maxSizeInBytes = 10 * 1024 * 1024 // 10MB
        if (sizeInBytes > maxSizeInBytes) {
            val sizeMB = sizeInBytes / (1024.0 * 1024.0)
            return ValidationResult(false, "Ukuran file terlalu besar (${String.format("%.2f", sizeMB)} MB). Maksimal 10 MB")
        }
        return ValidationResult(true, "")
    }
    
    fun validateFileFormat(fileName: String): ValidationResult {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        val allowedFormats = listOf("jpg", "jpeg", "png", "pdf")
        if (extension !in allowedFormats) {
            return ValidationResult(false, "Format file tidak didukung. Gunakan JPG, PNG, atau PDF")
        }
        return ValidationResult(true, "")
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String
)
