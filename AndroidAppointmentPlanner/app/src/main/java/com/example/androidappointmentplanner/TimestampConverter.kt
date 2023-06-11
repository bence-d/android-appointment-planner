package com.example.androidappointmentplanner

import android.provider.SyncStateContract.Constants
import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


object TimestampConverter {
    var df: DateFormat = SimpleDateFormat("YYYY-MM-DD")
    @TypeConverter

    fun fromTimestamp(value: String?): Date? {
        return if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }
}