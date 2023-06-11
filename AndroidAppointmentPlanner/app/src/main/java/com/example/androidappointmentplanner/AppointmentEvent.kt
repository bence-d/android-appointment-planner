package com.example.androidappointmentplanner

sealed class AppointmentEvent {
    data class SetTitleEvent(val title: String) : AppointmentEvent()
    data class DeleteAppointmentEvent(val appointment: Appointment) : AppointmentEvent()
    object SetAppointmentEvent : AppointmentEvent()
}
