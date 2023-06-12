package com.example.androidappointmentplanner

import java.time.LocalDateTime

sealed class AppointmentEvent {
    data class SetTitleEvent(val title: String) : AppointmentEvent()
    data class SetDescriptionEvent(val description: String) : AppointmentEvent()
    data class SetDateEvent(val date: LocalDateTime) : AppointmentEvent()
    data class SetCategoryEvent(val category: String) : AppointmentEvent()
    data class DeleteAppointmentEvent(val appointment: Appointment) : AppointmentEvent()
    object SetAppointmentEvent : AppointmentEvent()
    object SortByDateEvent : AppointmentEvent()
    object SortByCategoryEvent : AppointmentEvent()

    data class EditSavedAppointmentEvent(val appointment: Appointment) : AppointmentEvent()
    data class UpdateSavedAppointmentEvent(val appointment: Appointment) : AppointmentEvent()
}

