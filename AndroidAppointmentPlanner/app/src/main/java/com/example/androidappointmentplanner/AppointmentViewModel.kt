package com.example.androidappointmentplanner

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDateTime

class AppointmentViewModel(val dao: AppointmentDao) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _state = MutableStateFlow(AppointmentState())
    @RequiresApi(Build.VERSION_CODES.O)
    val state = combine(dao.findAll(), _state) { actAppoint, state ->
        state.copy(
            appointments = actAppoint
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppointmentState())

    @RequiresApi(Build.VERSION_CODES.O)
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
                try {
                    Log.d("STATE", "Starting persisting...")
                    viewModelScope.launch { dao.save(appointment) }
                    _state.update {
                        it.copy(
                            title = "",
                            description = "",
                            date = LocalDateTime.now()
                        )
                    }
                } catch (e : Exception) {
                    Log.d("STATE", "ERROR: " + e.message)
                }
            }
            is AppointmentEvent.SetTitleEvent -> {
                _state.update { it.copy(title = event.title) }
            }
            is AppointmentEvent.SetDescriptionEvent -> {
                _state.update { it.copy(description = event.description) }
            }
            is AppointmentEvent.SetDateEvent -> {
                _state.update { it.copy(date = event.date) }
            }
        }
    }
}