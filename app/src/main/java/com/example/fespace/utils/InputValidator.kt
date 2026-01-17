package com.example.fespace.utils

/**
 * Comprehensive input validation utility for user registration
 * Implements strict validation rules for security and data integrity
 */
object InputValidator {
    
    /**
     * Validate name: must start with letter and contain only letters
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validateName(name: String): Pair<Boolean, String> {
        if (name.isBlank()) {
            return Pair(false, "Nama tidak boleh kosong")
        }
        
        val trimmedName = name.trim()
        
        // Must start with a letter
        if (!trimmedName.first().isLetter()) {
            return Pair(false, "Nama harus diawali dengan huruf")
        }
        
        // Must contain only letters and spaces
        if (!trimmedName.all { it.isLetter() || it.isWhitespace() }) {
            return Pair(false, "Nama hanya boleh berisi huruf")
        }
        
        // Must have at least one letter (not just spaces)
        if (!trimmedName.any { it.isLetter() }) {
            return Pair(false, "Nama harus mengandung minimal satu huruf")
        }
        
        return Pair(true, "")
    }
    
    /**
     * Validate email: must be @gmail.com with proper format
     * Must contain combination of letters, numbers, uppercase, lowercase, symbols
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validateEmail(email: String): Pair<Boolean, String> {
        if (email.isBlank()) {
            return Pair(false, "Email tidak boleh kosong")
        }
        
        val trimmedEmail = email.trim()
        
        // Must end with @gmail.com
        if (!trimmedEmail.endsWith("@gmail.com", ignoreCase = true)) {
            return Pair(false, "Email harus menggunakan @gmail.com")
        }
        
        // Get the part before @gmail.com
        val localPart = trimmedEmail.substringBefore("@")
        
        if (localPart.isEmpty()) {
            return Pair(false, "Email tidak valid")
        }
        
        // Must contain at least one letter
        if (!localPart.any { it.isLetter() }) {
            return Pair(false, "Email harus mengandung huruf")
        }
        
        // Check for valid characters (letters, numbers, dots, underscores, hyphens)
        val validEmailChars = localPart.all { 
            it.isLetterOrDigit() || it == '.' || it == '_' || it == '-' 
        }
        
        if (!validEmailChars) {
            return Pair(false, "Email mengandung karakter tidak valid")
        }
        
        // Basic email format validation
        val emailPattern = "^[a-zA-Z0-9._-]+@gmail\\.com$".toRegex(RegexOption.IGNORE_CASE)
        if (!emailPattern.matches(trimmedEmail)) {
            return Pair(false, "Format email tidak valid")
        }
        
        return Pair(true, "")
    }
    
    /**
     * Validate WhatsApp number: accepts 10-12 digits starting with 8
     * Auto-formats to +62 format (Indonesian international format)
     * User input: 81366359496 (11 digits) → Stored as: +6281366359496 (14 chars total)
     * Example: 0813-6635-9496 → user enters 81366359496 → saved as +6281366359496
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validateWhatsApp(whatsapp: String): Pair<Boolean, String> {
        if (whatsapp.isBlank()) {
            return Pair(false, "Nomor WhatsApp tidak boleh kosong")
        }
        
        val trimmedNumber = whatsapp.trim()
        
        // Remove any spaces or dashes
        val cleanNumber = trimmedNumber.replace(" ", "").replace("-", "")
        
        // Check if it's already in +62 format (13-15 chars)
        if (cleanNumber.startsWith("+62")) {
            if (cleanNumber.length < 13 || cleanNumber.length > 15) {
                return Pair(false, "Nomor WhatsApp harus 10-12 digit")
            }
            val digitsAfterPrefix = cleanNumber.substring(3)
            if (!digitsAfterPrefix.all { it.isDigit() }) {
                return Pair(false, "Nomor WhatsApp hanya boleh berisi angka")
            }
            if (digitsAfterPrefix.first() != '8') {
                return Pair(false, "Nomor WhatsApp Indonesia harus diawali angka 8")
            }
            return Pair(true, "")
        }
        
        // If starts with 0, remove it (user might enter 08xxx)
        val numberWithoutZero = if (cleanNumber.startsWith("0")) {
            cleanNumber.substring(1)
        } else {
            cleanNumber
        }
        
        // Must be 10-12 digits (becomes 13-15 chars with +62)
        if (numberWithoutZero.length < 10 || numberWithoutZero.length > 12) {
            return Pair(false, "Nomor WhatsApp harus 10-12 digit")
        }
        
        // Must be all digits
        if (!numberWithoutZero.all { it.isDigit() }) {
            return Pair(false, "Nomor WhatsApp hanya boleh berisi angka")
        }
        
        // First digit must be 8 (Indonesian mobile numbers)
        if (numberWithoutZero.first() != '8') {
            return Pair(false, "Nomor WhatsApp Indonesia harus diawali angka 8")
        }
        
        return Pair(true, "")
    }
    
    /**
     * Validate password: minimum 8 characters with uppercase, lowercase, number, and symbol
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validatePassword(password: String): Pair<Boolean, String> {
        if (password.isBlank()) {
            return Pair(false, "Password tidak boleh kosong")
        }
        
        // Minimum 8 characters
        if (password.length < 8) {
            return Pair(false, "Password minimal 8 karakter")
        }
        
        // Must contain at least one uppercase letter
        if (!password.any { it.isUpperCase() }) {
            return Pair(false, "Password harus mengandung minimal 1 huruf besar")
        }
        
        // Must contain at least one lowercase letter
        if (!password.any { it.isLowerCase() }) {
            return Pair(false, "Password harus mengandung minimal 1 huruf kecil")
        }
        
        // Must contain at least one digit
        if (!password.any { it.isDigit() }) {
            return Pair(false, "Password harus mengandung minimal 1 angka")
        }
        
        // Must contain at least one symbol
        val symbols = "!@#$%^&*()_+-=[]{}|;:',.<>?/~`"
        if (!password.any { it in symbols }) {
            return Pair(false, "Password harus mengandung minimal 1 simbol (!@#$%^&* dll)")
        }
        
        return Pair(true, "")
    }
    
    /**
     * Validate all registration fields at once
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validateRegistration(
        name: String,
        email: String,
        whatsapp: String,
        password: String
    ): Pair<Boolean, String> {
        // Validate name
        val nameValidation = validateName(name)
        if (!nameValidation.first) return nameValidation
        
        // Validate email
        val emailValidation = validateEmail(email)
        if (!emailValidation.first) return emailValidation
        
        // Validate WhatsApp
        val whatsappValidation = validateWhatsApp(whatsapp)
        if (!whatsappValidation.first) return whatsappValidation
        
        // Validate password
        val passwordValidation = validatePassword(password)
        if (!passwordValidation.first) return passwordValidation
        
        return Pair(true, "")
    }
    
    /**
     * Validate text fields: must start with letter and contain only letters
     * @param fieldName Display name of the field for the error message
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validateGenericText(text: String, fieldName: String): Pair<Boolean, String> {
        if (text.isBlank()) {
            return Pair(false, "$fieldName tidak boleh kosong")
        }
        
        val trimmedText = text.trim()
        
        // Must start with a letter
        if (!trimmedText.first().isLetter()) {
            return Pair(false, "$fieldName harus diawali dengan huruf")
        }
        
        // Must contain only letters and spaces
        if (!trimmedText.all { it.isLetter() || it.isWhitespace() }) {
            return Pair(false, "$fieldName hanya boleh berisi huruf")
        }
        
        return Pair(true, "")
    }

    /**
     * Validate service/portfolio name and description
     * Must contain combination of letters, numbers, and symbols
     * @return Pair<Boolean, String> - (isValid, errorMessage)
     */
    fun validateServiceName(name: String): Pair<Boolean, String> {
        if (name.isBlank()) {
            return Pair(false, "Nama tidak boleh kosong")
        }
        
        val trimmedName = name.trim()
        
        // Must contain at least one letter
        if (!trimmedName.any { it.isLetter() }) {
            return Pair(false, "Harus mengandung minimal 1 huruf")
        }
        
        // Must contain at least one digit OR symbol
        val hasDigit = trimmedName.any { it.isDigit() }
        val hasSymbol = trimmedName.any { !it.isLetterOrDigit() && !it.isWhitespace() }
        
        if (!hasDigit && !hasSymbol) {
            return Pair(false, "Harus kombinasi huruf dengan angka atau simbol")
        }
        
        return Pair(true, "")
    }
    
    /**
     * Validate description field
     * Must contain combination of letters with numbers or symbols
     */
    fun validateDescription(description: String): Pair<Boolean, String> {
        if (description.isBlank()) {
            return Pair(false, "Deskripsi tidak boleh kosong")
        }
        
        val trimmed = description.trim()
        
        // Must contain at least one letter
        if (!trimmed.any { it.isLetter() }) {
            return Pair(false, "Deskripsi harus mengandung minimal 1 huruf")
        }
        
        // Must contain at least one digit OR symbol
        val hasDigit = trimmed.any { it.isDigit() }
        val hasSymbol = trimmed.any { !it.isLetterOrDigit() && !it.isWhitespace() }
        
        if (!hasDigit && !hasSymbol) {
            return Pair(false, "Deskripsi harus kombinasi huruf dengan angka atau simbol")
        }
        
        return Pair(true, "")
    }
    
    /**
     * Format WhatsApp number to +62 format if user enters without prefix
     * Converts: 08123456789 -> +628123456789
     */
    fun formatWhatsAppNumber(input: String): String {
        val cleaned = input.trim().replace(" ", "").replace("-", "")
        
        return when {
            cleaned.startsWith("+62") -> cleaned
            cleaned.startsWith("62") -> "+$cleaned"
            cleaned.startsWith("0") -> "+62${cleaned.substring(1)}"
            else -> "+62$cleaned"
        }
    }
}
