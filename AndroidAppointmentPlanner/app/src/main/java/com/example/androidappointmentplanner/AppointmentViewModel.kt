package com.example.androidappointmentplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppointmentViewModel(private val dao: AppointmentDao) : ViewModel() {

    private val _state = MutableStateFlow(AppointmentState())

    val state = combine(dao.findAll(), _state) { appointments, state ->
        state.copy(appointments = appointments)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppointmentState())

    fun handleEvent(event: AppointmentEvent) {
        when (event) {
            is AppointmentEvent.SetTitleEvent -> {
                _state.update { it.copy(title = event.title) }
            }
            is AppointmentEvent.SetDescriptionEvent -> {
                _state.update { it.copy(description = event.description) }
            }
            is AppointmentEvent.SetDateEvent -> {
                _state.update { it.copy(date = event.date) }
            }
            is AppointmentEvent.SetCategoryEvent -> {
                _state.update { it.copy(category = event.category) }
            }
            is AppointmentEvent.DeleteAppointmentEvent -> {
                viewModelScope.launch {
                    dao.delete(event.appointment)
                }
            }
            is AppointmentEvent.EditSavedAppointmentEvent -> {
                _state.update { it.copy(editableAppointment = event.appointment) }
            }

            is AppointmentEvent.UpdateSavedAppointmentEvent -> {
                viewModelScope.launch {
                    dao.update(event.appointment)
                    _state.update { it.copy(editableAppointment = null) }
                }
            }

            else -> {}
        }
    }
}
