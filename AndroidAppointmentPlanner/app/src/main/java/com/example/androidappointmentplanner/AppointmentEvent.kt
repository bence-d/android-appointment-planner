package com.example.androidappointmentplanner

sealed class AppointmentEvent {
    data class SetTitleEvent(val title: String) : AppointmentEvent()
    data class SetDescriptionEvent(val description: String) : AppointmentEvent()
    data class SetDateEvent(val date: String) : AppointmentEvent()
    data class DeleteAppointmentEvent(val appointment: Appointment) : AppointmentEvent()
    object SetAppointmentEvent : AppointmentEvent()
}
