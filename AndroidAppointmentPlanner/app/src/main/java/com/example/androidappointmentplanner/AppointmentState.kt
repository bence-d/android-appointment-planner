package com.example.androidappointmentplanner

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.time.LocalDateTime

data class AppointmentState(
    val title: String = "",
    val description: String = "",
    val date: LocalDateTime = LocalDateTime.MIN,
    val category: String = "",
    val appointments: List<Appointment> = emptyList(),
    val editableAppointment: Appointment? = null
)