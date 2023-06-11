package com.example.androidappointmentplanner

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.sql.Date
import java.time.LocalDateTime

@Entity
data class Appointment(
    val title: String,
    val description: String,
    val date: LocalDateTime, // MM-DD-YYYY
    // Uhrzeit
    // Kategorie

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)