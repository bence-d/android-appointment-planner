package com.example.androidappointmentplanner

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Date
import java.time.LocalDateTime

data class AppointmentState constructor(
    val appointments: List<Appointment> = emptyList(),
    val title: String = "",
    val description: String = "",
    val date: LocalDateTime = LocalDateTime.MIN
)