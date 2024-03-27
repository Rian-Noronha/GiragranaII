package com.rnoronha.giragrana.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun getCurrentFormattedDate(): String {
        val currentDate = Calendar.getInstance().time
        return dateFormat.format(currentDate)
    }
}