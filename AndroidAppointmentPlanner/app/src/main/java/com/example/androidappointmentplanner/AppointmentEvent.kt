package com.example.androidappointmentplanner

import java.sql.Date
import java.time.LocalDateTime

sealed class AppointmentEvent {
    data class SetTitleEvent(val title: String) : AppointmentEvent()
    data class SetDescriptionEvent(val description: String) : AppointmentEvent()
    data class SetDateEvent(val date: LocalDateTime) : AppointmentEvent()
    data class DeleteAppointmentEvent(val appointment: Appointment) : AppointmentEvent()
    object SetAppointmentEvent : AppointmentEvent()


}
