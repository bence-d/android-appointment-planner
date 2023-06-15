package com.example.androidappointmentplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.androidappointmentplanner.ui.theme.AndroidAppointmentPlannerTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date

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
                    return AppointmentViewModel(db.appointmentDao()) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Navigation(db, viewModel)

            /*
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
                        NewAppointmentForm(state = state.value, appointmentEvent = viewModel::handleEvent)
                        Divider(color = Color.Blue, thickness = 1.dp)
                        AppointmentList(state = state.value, appointmentEvent = viewModel::handleEvent)
                    }

                }
            }

             */
        }
    }
}

/*
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
*/
