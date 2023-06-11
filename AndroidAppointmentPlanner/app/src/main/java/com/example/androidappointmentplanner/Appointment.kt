package com.example.androidappointmentplanner

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.sql.Date

@Entity
data class Appointment(
    val title: String,
    val description: String,
    val date: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)