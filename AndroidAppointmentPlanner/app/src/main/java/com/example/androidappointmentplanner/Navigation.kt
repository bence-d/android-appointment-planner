package com.example.androidappointmentplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date

/* Ein Appointment wird im Appointment.tk Klasse beschrieben */
// data class Customer(val userName: String, val userId: String)

// Wird durch state.appointments ersetzt
/*
val customers = listOf<Customer>(
    Customer("Max", "1"),
    Customer("Peter", "2"),
    Customer("Susi", "3")
)
 */

@Composable
fun Navigation(
    db: AppointmentDatabase,
    viewModel: AppointmentViewModel
) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable(route = "main") {
            AppointmentList(state = viewModel.state.collectAsState(), appointmentEvent = viewModel::handleEvent, navController = navController)
        }
        composable(
            route = "detail/{appointmentId}",
            arguments = listOf(
                navArgument("appointmentId") {
                    type = NavType.StringType
                }
            )
        ) {
            DetailScreen(
                navController = navController,
                appointmentId = it.arguments?.getString("appointmentId"),
                state = viewModel.state.collectAsState()
            )
        }

        composable(
            route = "detail/create"
        ) {
            NewAppointmentForm(
                state = viewModel.state.collectAsState(),
                appointmentEvent = viewModel::handleEvent,
                navController = navController)
        }
    }
}

// Custom

@Composable
fun AppointmentList(
    state: State<AppointmentState>,
    appointmentEvent: (AppointmentEvent) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button (
            onClick = {
                navController.navigate("detail/create")
            },
            modifier = Modifier
                .padding(5.dp, 5.dp, 0.dp, 50.dp)
                .width(IntrinsicSize.Max)
        ) {
            Text(text = "Termin erstellen")
        }

        for (appointment in state.value.appointments) {
            Row() {
                Text(
                    text = appointment.title,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate("detail/${appointment.id}")
                        }
                        .padding(10.dp)

                )
                Text(
                    text = appointment.id.toString(),
                    fontSize = 25.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)

                )
                IconButton(
                    onClick = {  appointmentEvent(AppointmentEvent.DeleteAppointmentEvent(appointment)) },
                    modifier = Modifier
                        .size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Appointment",
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    navController: NavController, appointmentId: String?,
    state: State<AppointmentState>,
) {
    var appointmentIdRes = 0;

    // to-do: Fehlermeldung im Fall aber es sollte eh nie vorkommen?
    if (appointmentId == null) {
        navController.navigate("main")
    } else {
        appointmentIdRes = appointmentId.toInt();
    }

    val appointment =
        state.value.appointments.first  { it.id == appointmentIdRes }

    Text(
        text = "Titel: ${appointment.title}",
        color = Color.Blue,
        fontSize = 30.sp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(0.dp, 5.dp)
    )
    Text(
        text = "Beschreibung: ${appointment.description}",
        color = Color.Blue,
        fontSize = 30.sp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(0.dp, 50.dp)
            .width(IntrinsicSize.Max)
    )
    Text(
        text = "Datum: ${appointment.date.toString()}",
        color = Color.Blue,
        fontSize = 30.sp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(0.dp, 100.dp)
            .width(IntrinsicSize.Max)
    )

    Button (
        onClick = {
            navController.navigate("main")
        },
        modifier = Modifier
            .padding(5.dp, 200.dp)
            .width(IntrinsicSize.Max)
    ) {
        Text(text = "Zurück")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointmentForm(
    state: State<AppointmentState>,
    appointmentEvent: (AppointmentEvent) -> Unit,
    navController: NavController
) {
    Column() {
        // Title
        Row() {
            TextField(
                value = state.value.title,
                onValueChange = {
                    appointmentEvent(AppointmentEvent.SetTitleEvent(it))
                },
                label = { Text(text = "Titel") }
            )
        }

        // Description
        Row() {
            TextField(
                value = state.value.description,
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
                    mDate.value = ""

                    if ((mDayOfMonth + 1) < 10) {
                        mDate.value += "0${mDayOfMonth}";
                    } else {
                        mDate.value += mDayOfMonth
                    }

                    mDate.value += "/"

                    if ((mMonth + 1) < 10) {
                        mDate.value += "0${mMonth+1}";
                    } else {
                        mDate.value += mMonth+1
                    }

                    mDate.value += "/$mYear";
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
                    Log.d("STATE", mTime.value)
                    Log.d("STATE", mDate.value)

                    val stringDate = "${mDate.value.format("dd/MM/yyyy")} ${mTime.value}";
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                    var dt = LocalDateTime.now();
                    try {
                        dt = LocalDateTime.parse(stringDate, formatter)
                    } catch (e : DateTimeParseException) {
                        Log.d("STATE","cant parse [$stringDate]")
                        Log.d("STATE", "to [$formatter]")
                    }

                    AppointmentEvent.SetDateEvent(dt)
                    appointmentEvent(AppointmentEvent.SetAppointmentEvent)
                    navController.navigate("main")
                }
            ) {
                Text(text = "Save")
            }
        }

        Button (
            onClick = {
                navController.navigate("main")
            }
        ) {
            Text(text = "Zurück")
        }
    }
}

// Originale

/*
@Composable
fun MainScreen(navController: NavController) {
    Column() {
        for (customer in customers) {
            Text(
                text = "${customer.userId} - ${customer.userName}",
                color = Color.Blue,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(5.dp)
                    .clickable {
                        navController.navigate("detail/${customer.userId}")
                    }
            )
        }
    }
}

@Composable
fun DetailScreen(navController: NavController, customerId: String?) {
    val customer = customers.first { it.userId == customerId }
    Text(
        text = "${customer.userName}",
        color = Color.Blue,
        fontSize = 30.sp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(5.dp)
    )
}
 */



