package com.example.androidappointmentplanner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.sql.Date
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {

        try {
            return value?.let { LocalDateTime.parse(it) }
        } catch (e : DateTimeParseException) {
            return value.let { LocalDateTime.now() }
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}