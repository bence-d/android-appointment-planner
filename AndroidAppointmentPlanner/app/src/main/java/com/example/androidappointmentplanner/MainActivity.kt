package com.example.androidappointmentplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.androidappointmentplanner.ui.theme.AndroidAppointmentPlannerTheme

class MainActivity : ComponentActivity() {
    // Building database
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppointmentDatabase::class.java,
            "persons.db"
        ).build()
    }

    // Instancing ViewModel
    private val viewModel by viewModels<AppointmentViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                 override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppointmentViewModel(db.personDao()) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAppointmentPlannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = viewModel.state.collectAsState()
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //AppointmentList(state = state.value, appointmentEvent = viewModel::handleEvent)
                        NewAppointmentForm(state = state.value, appointmentEvent = viewModel::handleEvent)
                        Divider(color = Color.Blue, thickness = 1.dp)
                        AppointmentList(state = state.value, appointmentEvent = viewModel::handleEvent)
                    }

                }
            }
        }
    }
}

@Composable
fun AppointmentList(
    state: AppointmentState,
    appointmentEvent: (AppointmentEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (appointment in state.appointments) {
            Row() {
                Text(
                    text = appointment.title,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)

                )
                IconButton(onClick = {
                    appointmentEvent(AppointmentEvent.DeleteAppointmentEvent(appointment))
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Appointment",
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointmentForm(
    state: AppointmentState,
    appointmentEvent: (AppointmentEvent) -> Unit
) {
    Row() {
        TextField(
            value = state.title,
            onValueChange = {
                appointmentEvent(AppointmentEvent.SetTitleEvent(it))
            },
            label = { Text(text = "Name") }
        )
        Button(
            onClick = { appointmentEvent(AppointmentEvent.SetAppointmentEvent) }
        ) {
            Text(text = "Save")
        }
    }
}