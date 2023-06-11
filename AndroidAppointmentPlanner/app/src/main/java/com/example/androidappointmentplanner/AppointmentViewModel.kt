package com.example.androidappointmentplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppointmentViewModel(val dao: AppointmentDao) : ViewModel() {

    private val _state = MutableStateFlow(AppointmentState())
    val state = combine(dao.findAll(), _state) { actAppoint, state ->
        state.copy(
            appointments = actAppoint
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppointmentState())

    fun handleEvent(event: AppointmentEvent) {
        when (event) {
            is AppointmentEvent.DeleteAppointmentEvent -> {
                viewModelScope.launch { dao.delete( event.appointment ) }
            }
            AppointmentEvent.SetAppointmentEvent -> {
                val title = state.value.title
                val desc = state.value.description
                val date = state.value.date
                if (title.isBlank()) return
                val appointment = Appointment(title, desc, date)
                viewModelScope.launch { dao.save(appointment) }
                _state.update {
                    it.copy(
                        title = ""
                    )
                }
            }
            is AppointmentEvent.SetTitleEvent -> {
                _state.update { it.copy(title = event.title) }
            }
        }
    }
}