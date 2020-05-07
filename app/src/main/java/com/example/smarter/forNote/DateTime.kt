package com.example.smarter.forNote

import java.text.SimpleDateFormat
import java.util.*

class DateTime {
    fun dateTime(): String? {
        val cal : Calendar = Calendar.getInstance()
        val f = SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss", Locale.US)
        return f.format(cal.time)
//.SS
    }
}