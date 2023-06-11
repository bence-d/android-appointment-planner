package com.example.androidappointmentplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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
    // Title
    Row() {
        TextField(
            value = state.title,
            onValueChange = {
                appointmentEvent(AppointmentEvent.SetTitleEvent(it))
            },
            label = { Text(text = "Titel") }
        )
    }

    // Description
    Row() {
        TextField(
            value = state.description,
            onValueChange = {
                appointmentEvent(AppointmentEvent.SetDescriptionEvent(it))
            },
            label = { Text(text = "Beschreibung") }
        )
    }

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Calendar
    Row() {

        // Fetching the Local Context
        val mContext = LocalContext.current

        // Declaring integer values
        // for year, month and day
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        mCalendar.time = Date()

        // Declaring DatePickerDialog and setting
        // initial values as current values (present year, month and day)
        val mDatePickerDialog = DatePickerDialog(
            mContext,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            }, mYear, mMonth, mDay
        )

        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            // Displaying the mDate value in the Text
            Text(text = "Selected Date: ${mDate.value}", fontSize = 22.sp, textAlign = TextAlign.Center)
        }


        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { mDatePickerDialog.show() }
            ) {
                Text(text = "Save")
            }
        }
    }

    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }

    // Time
    Row() {
        // Fetching local context
        val mContext = LocalContext.current

        // Declaring and initializing a calendar
        val mCalendar = Calendar.getInstance()
        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]

        // Creating a TimePicker dialod
        val mTimePickerDialog = TimePickerDialog(
            mContext,
            {_, mHour : Int, mMinute: Int ->
                mTime.value = "$mHour:$mMinute"
            }, mHour, mMinute, false
        )

        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            // Displaying the mDate value in the Text
            Text(text = "Selected Time: ${mTime.value}", fontSize = 22.sp, textAlign = TextAlign.Center)
        }


        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { mTimePickerDialog.show() }
            ) {
                Text(text = "Save")
            }
        }
    }

    // Save
    Row() {
        Button(
            onClick = {
                appointmentEvent(AppointmentEvent.SetAppointmentEvent)
            }
        ) {
            Text(text = "Save")
        }
    }
}