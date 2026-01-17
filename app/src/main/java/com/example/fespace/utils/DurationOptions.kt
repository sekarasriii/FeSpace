package com.example.fespace.utils

/**
 * Provides duration options for service estimation (simplified)
 */
object DurationOptions {
    
    /**
     * Get simplified, practical duration options
     */
    fun getFlattenedOptions(): List<String> {
        return listOf(
            // Days
            "1 hari",
            "2 hari",
            "3 hari",
            "5 hari",
            "7 hari",
            // Weeks
            "1 minggu",
            "2 minggu",
            "3 minggu",
            // Months
            "1 bulan",
            "2 bulan",
            "3 bulan",
            "6 bulan",
            // Years
            "1 tahun",
            // Ranges (commonly used)
            "1-2 hari",
            "3-5 hari",
            "1-2 minggu",
            "1-2 bulan",
            "3-6 bulan"
        )
    }
    
    // Legacy functions kept for compatibility
    fun getDayOptions(): List<String> = (1..31).map { "$it hari" }
    fun getMonthOptions(): List<String> = (1..12).map { "$it bulan" }
    fun getYearOptions(): List<String> = (1..5).map { "$it tahun" }
    fun getRangeOptions(): List<String> = listOf(
        "1-2 hari", "3-5 hari", "1-2 minggu", "2-3 minggu",
        "1-2 bulan", "2-3 bulan", "3-6 bulan", "6-12 bulan"
    )
    fun getAllOptions(): Map<String, List<String>> = mapOf(
        "Hari" to getDayOptions().take(14),
        "Minggu" to listOf("1 minggu", "2 minggu", "3 minggu", "4 minggu"),
        "Bulan" to getMonthOptions(),
        "Tahun" to getYearOptions(),
        "Range" to getRangeOptions()
    )
}
