package com.example.androidappointmentplanner

import java.sql.Date

data class AppointmentState (
    val appointments: List<Appointment> = emptyList(),
    val title: String = "",
    val description: String = "",
    val date: String = ""
)