package com.example.androidappointmentplanner

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@TypeConverters(Converters::class)
@Entity
data class Appointment(
    val title: String,
    val description: String,
    val date: LocalDateTime,
    val category: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)


