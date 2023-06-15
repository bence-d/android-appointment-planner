package com.example.androidappointmentplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
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
            AppointmentList(state = viewModel.state.collectAsState(), navController = navController)
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
                state = viewModel.state.collectAsState(),
                appointmentEvent = viewModel::handleEvent
            )
        }

        composable(
            route = "detail/create/{appointmentId}",
            arguments = listOf(
                navArgument("appointmentId") {
                    type = NavType.StringType
                }
            )
        ) {
            NewAppointmentForm(
                state = viewModel.state.collectAsState(),
                appointmentEvent = viewModel::handleEvent,
                navController = navController,
                appointmentId = it.arguments?.getString("appointmentId")
            )
        }
    }
}

// Custom

@Composable
fun AppointmentList(
    state: State<AppointmentState>,
    navController: NavController
) {
    Log.d("State", "entered appointmentlist..")

    Log.d("State", "reading items..")
    var appointments = state.value.appointments;

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsv(0.0f, 0.0f, 0.14f, 1f, colorSpace = ColorSpaces.Srgb))
    ) {
        Text(
            "TERMINE",
            color = Color.hsv(345.0f, 1.0f, 0.73f, 1f, colorSpace = ColorSpaces.Srgb),
            fontSize = 35.sp,
            fontFamily = fontFamily,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 30.dp, 0.dp, 20.dp)
        )

        Divider(
            color = Color.hsv(0.0f, 0.0f, 1f, 0.3f, colorSpace = ColorSpaces.Srgb),
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 0.dp, 0.dp, 30.dp)
        )

        // Add Appointment

        Log.d("state", "Generating add apointment button")
        Button(
            onClick = {
                navController.navigate("detail/create/-1")
            },
            colors = ButtonDefaults.buttonColors(

                containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            border = BorderStroke(
                0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 20.dp)
                .height(55.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)

        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Appointment",
            )
        }
        Log.d("state", "after generating button...")

        // Temine löschen

        if (state.value.appointments.isNotEmpty()) {
            Log.d("State", "Trying to Listing out appointments...")
            for (appointment in state.value.appointments) {

                if (appointment.date < LocalDateTime.now()) {
                    // Example Appointment
                    Button(
                        onClick = {
                            navController.navigate("detail/${appointment.id}")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.hsv(
                                345.0f,
                                1.0f,
                                0.43f,
                                1.0f,
                                colorSpace = ColorSpaces.Srgb
                            )
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color.hsv(0.0f, 0.0f, 1.0f, 0.25f, colorSpace = ColorSpaces.Srgb)
                        ),
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            .padding(0.dp, 5.dp, 0.dp, 20.dp)
                            .height(55.dp)
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Text(text = appointment.title)
                    }
                } else {
                    // Example Appointment
                    Button(
                        onClick = {
                            navController.navigate("detail/${appointment.id}")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.hsv(
                                0.0f,
                                0.0f,
                                0.31f,
                                0.0f,
                                colorSpace = ColorSpaces.Srgb
                            )
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color.hsv(0.0f, 0.0f, 1.0f, 0.25f, colorSpace = ColorSpaces.Srgb)
                        ),
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            .padding(0.dp, 5.dp, 0.dp, 20.dp)
                            .height(55.dp)
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Text(text = appointment.title)
                    }
                }
                Log.d("State", "Listing out appointments...")

            }
        } else {
            Log.d("state", "state is empty")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    appointmentId: String?,
    state: State<AppointmentState>,
    appointmentEvent: (AppointmentEvent) -> Unit
) {
    Log.d("State", "entered detailscreen")

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontName = GoogleFont("Iceland")

    val fontFamily = FontFamily(
        Font(
            googleFont = fontName,
            fontProvider = provider,
            weight = FontWeight.W400,
            style= FontStyle.Normal
        )
    )


    var appointmentIdRes = 0;

    // to-do: Fehlermeldung im Fall aber es sollte eh nie vorkommen?
    if (appointmentId == null) {
        Log.d("State", "appointmentId null -> returning to main")
        //navController.navigate("main")
    } else {
        appointmentIdRes = appointmentId.toInt();
    }

    val appointment =
        state.value.appointments.firstOrNull { it.id == appointmentIdRes }

    if (appointment == null) {
        Log.d("State", "appointment object null -> returning to main")
        //navController.navigate("main")
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.hsv(0.0f, 0.0f, 0.14f, 1f, colorSpace = ColorSpaces.Srgb))
        ) {
            Text(
                "TERMIN DETAILS",
                color = Color.hsv(345.0f, 1.0f, 0.73f, 1f, colorSpace = ColorSpaces.Srgb),
                fontSize = 35.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 30.dp, 0.dp, 20.dp)
            )

            Divider(
                color = Color.hsv(0.0f, 0.0f, 1f, 0.3f, colorSpace = ColorSpaces.Srgb),
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 0.dp, 0.dp, 30.dp)
            )

            OutlinedTextField(
                value = appointment.title,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.0f, colorSpace = ColorSpaces.Srgb),
                    textColor = Color.White,
                    focusedLabelColor = Color.hsv(0.0f, 0.0f, 1f, 0.5f, colorSpace = ColorSpaces.Srgb),
                    focusedIndicatorColor = Color.hsv(
                        0.0f,
                        0.0f,
                        1f,
                        0.5f,
                        colorSpace = ColorSpaces.Srgb
                    ),
                    disabledTextColor = Color.White,
                    disabledLabelColor = Color.White

                ),
                onValueChange = {

                },
                label = { Text(text = "Titel") },
                singleLine = true,
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally),
                readOnly = true
            )

            OutlinedTextField(
                value = appointment.description,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.0f, colorSpace = ColorSpaces.Srgb),
                    textColor = Color.White,
                    focusedLabelColor = Color.hsv(0.0f, 0.0f, 1f, 0.5f, colorSpace = ColorSpaces.Srgb),
                    focusedIndicatorColor = Color.hsv(
                        0.0f,
                        0.0f,
                        1f,
                        0.5f,
                        colorSpace = ColorSpaces.Srgb
                    ),
                    disabledTextColor = Color.White,
                    disabledLabelColor = Color.White

                ),
                onValueChange = {

                },
                label = { Text(text = "Beschreibung") },
                singleLine = false,
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                    .height(100.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally),
                readOnly = true
            )

            OutlinedTextField(
                value = "Datum: " + appointment.date.dayOfMonth + "/" + appointment.date.monthValue + "/" + appointment.date.year,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.0f, colorSpace = ColorSpaces.Srgb),
                    textColor = Color.White,
                    focusedLabelColor = Color.hsv(0.0f, 0.0f, 1f, 0.5f, colorSpace = ColorSpaces.Srgb),
                    focusedIndicatorColor = Color.hsv(
                        0.0f,
                        0.0f,
                        1f,
                        0.5f,
                        colorSpace = ColorSpaces.Srgb
                    ),
                    disabledTextColor = Color.White,
                    disabledLabelColor = Color.White

                ),
                onValueChange = {

                },
                label = { Text(text = "Titel") },
                singleLine = true,
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally),
                readOnly = true
            )

            OutlinedTextField(
                value = "Zeit: " + appointment.date.hour + ":" + appointment.date.minute,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.0f, colorSpace = ColorSpaces.Srgb),
                    textColor = Color.White,
                    focusedLabelColor = Color.hsv(0.0f, 0.0f, 1f, 0.5f, colorSpace = ColorSpaces.Srgb),
                    focusedIndicatorColor = Color.hsv(
                        0.0f,
                        0.0f,
                        1f,
                        0.5f,
                        colorSpace = ColorSpaces.Srgb
                    ),
                    disabledTextColor = Color.White,
                    disabledLabelColor = Color.White

                ),
                onValueChange = {

                },
                label = { Text(text = "Zeit") },
                singleLine = true,
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally),
                readOnly = true
            )

            // Edit

            Button(
                onClick = {
                    navController.navigate("detail/create/${appointmentId}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
                ),
                border = BorderStroke(
                    0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
                ),
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                    .height(55.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Termin bearbeiten",
                )
            }

            // Löschen

            for (actAppointment in state.value.appointments) {
                if (appointmentId != null) {
                    if (actAppointment.id == appointmentId.toInt()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Button(
                                onClick = {
                                    Log.d("State", "Termin löschen...")
                                    appointmentEvent(AppointmentEvent.DeleteAppointmentEvent(actAppointment))
                                    navController.navigate("main")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.hsv(
                                        0.0f,
                                        0.0f,
                                        0.31f,
                                        0.5f,
                                        colorSpace = ColorSpaces.Srgb
                                    )
                                ),
                                border = BorderStroke(
                                    0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
                                ),
                                shape = RoundedCornerShape(10),
                                modifier = Modifier
                                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                                    .height(55.dp)
                                    .fillMaxWidth(0.9f)

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Termin Löschen",
                                )
                            }
                        }
                        break;
                    }
                }
            }

            // Zurück

            Button(
                onClick = {
                    navController.navigate("main")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
                ),
                border = BorderStroke(
                    0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
                ),
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .padding(0.dp, 5.dp, 0.dp, 20.dp)
                    .height(55.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)

            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Zurück zum Menü",
                )
            }
        }
    }
}

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Iceland")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.W400,
        style= FontStyle.Normal
    )
)

var latestEditedID = -1;

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointmentForm(
    state: State<AppointmentState>,
    appointmentEvent: (AppointmentEvent) -> Unit,
    navController: NavController,
    appointmentId: String?
) {
    if (appointmentId != null && appointmentId.toInt() != -1 && latestEditedID != appointmentId.toInt()) {
        val appointmentIdRes = appointmentId.toInt();
        latestEditedID = appointmentIdRes;

        val appointment =
            state.value.appointments.first { it.id == appointmentIdRes }

        Log.d("STATE", "Setting stuff...")
        appointmentEvent(AppointmentEvent.SetTitleEvent(appointment.title))
        appointmentEvent(AppointmentEvent.SetDescriptionEvent(appointment.description))
        appointmentEvent(AppointmentEvent.SetDateEvent(appointment.date))
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsv(0.0f, 0.0f, 0.14f, 1f, colorSpace = ColorSpaces.Srgb))
    ) {
        Text(
            "TERMIN BEARBEITEN",
            color = Color.hsv(345.0f, 1.0f, 0.73f, 1f, colorSpace = ColorSpaces.Srgb),
            fontSize = 35.sp,
            fontFamily = fontFamily,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 30.dp, 0.dp, 20.dp)
        )

        Divider(
            color = Color.hsv(0.0f, 0.0f, 1f, 0.3f, colorSpace = ColorSpaces.Srgb),
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 0.dp, 0.dp, 30.dp)
        )

        // Title

        val maxTitleLength = 30

        OutlinedTextField(
            value = state.value.title,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.0f, colorSpace = ColorSpaces.Srgb),
                textColor = Color.White,
                focusedLabelColor = Color.hsv(0.0f, 0.0f, 1f, 0.5f, colorSpace = ColorSpaces.Srgb),
                focusedIndicatorColor = Color.hsv(
                    0.0f,
                    0.0f,
                    1f,
                    0.5f,
                    colorSpace = ColorSpaces.Srgb
                )

            ),
            onValueChange = {
                if (it.length <= maxTitleLength) appointmentEvent(AppointmentEvent.SetTitleEvent(it))
            },

            label = { Text(text = "Titel") },
            singleLine = true,
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 20.dp)
                .height(80.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            supportingText = {
                Text(
                    text = "${state.value.title.length} / $maxTitleLength",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        val maxDescLength = 200

        OutlinedTextField(
            value = state.value.description,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.0f, colorSpace = ColorSpaces.Srgb),
                textColor = Color.White,
                focusedLabelColor = Color.hsv(0.0f, 0.0f, 1f, 0.5f, colorSpace = ColorSpaces.Srgb),
                focusedIndicatorColor = Color.hsv(
                    0.0f,
                    0.0f,
                    1f,
                    0.5f,
                    colorSpace = ColorSpaces.Srgb
                )
            ),
            onValueChange = {
                if (it.length <= maxDescLength) appointmentEvent(
                    AppointmentEvent.SetDescriptionEvent(
                        it
                    )
                )
            },
            label = { Text(text = "Beschreibung") },
            singleLine = false,
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 20.dp)
                .height(155.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            supportingText = {
                Text(
                    text = "${state.value.description.length} / $maxDescLength",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        // Datum

        val mDate = remember { mutableStateOf("") }

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
                    mDate.value += "0${mMonth + 1}";
                } else {
                    mDate.value += mMonth + 1
                }

                mDate.value += "/$mYear";
            }, mYear, mMonth, mDay
        )

        Button(
            onClick = {
                mDatePickerDialog.show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsv(0.0f, 0.0f, 0.5f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            border = BorderStroke(
                0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 10.dp)
                .height(55.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)

        ) {
            Text(
                text = "Datum   "
            )
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Datum Auswählen",
            )

        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.0.dp, 0.0.dp, 0.0.dp, 5.0.dp)
        ) {
            Text(
                text = "Selected Date: ${mDate.value}",
                fontSize = 15.sp,
                color = Color.hsv(0.0f, 0.0f, 1.0f, 0.2f, colorSpace = ColorSpaces.Srgb),
                textAlign = TextAlign.Center
            )
        }

        // Time

        val mTime = remember { mutableStateOf("") }
        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]

        // Creating a TimePicker dialog
        val mTimePickerDialog = TimePickerDialog(
            mContext,
            { _, mHour: Int, mMinute: Int ->
                mTime.value = String.format("%02d:%02d", mHour, mMinute)
            }, mHour, mMinute, false
        )

        Button(
            onClick = {
                mTimePickerDialog.show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsv(0.0f, 0.0f, 0.5f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            border = BorderStroke(
                0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 10.dp)
                .height(55.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)

        ) {
            Text(
                text = "Zeit   "
            )
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Zeit Auswählen",
            )

        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.0.dp, 0.0.dp, 0.0.dp, 5.0.dp)
        ) {
            Text(
                text = "Selected Time: ${mTime.value}",
                fontSize = 15.sp,
                color = Color.hsv(0.0f, 0.0f, 1.0f, 0.2f, colorSpace = ColorSpaces.Srgb),
                textAlign = TextAlign.Center
            )
        }

        // Add Appointment

        Button(
            onClick = {
                Log.d("STATE", mTime.value)
                Log.d("STATE", mDate.value)

                val stringDate = "${mDate.value.format("dd/MM/yyyy")} ${mTime.value}";
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                var dt = LocalDateTime.now();
                try {
                    dt = LocalDateTime.parse(stringDate, formatter)
                } catch (e: DateTimeParseException) {
                    Log.d("STATE", "cant parse [$stringDate]")
                    Log.d("STATE", "to [$formatter]")
                }

                appointmentEvent(AppointmentEvent.SetDateEvent(dt))

                if (appointmentId != null && appointmentId.toInt() != -1) {
                    val appointmentIdRes = appointmentId.toInt();

                    val appointment =
                        state.value.appointments.first { it.id == appointmentIdRes }
                    appointmentEvent(AppointmentEvent.DeleteAppointmentEvent(appointment = appointment))
                    appointmentEvent(AppointmentEvent.SetAppointmentEvent)
                    navController.navigate("main")
                } else {
                    appointmentEvent(AppointmentEvent.SetAppointmentEvent)
                    navController.navigate("main")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            border = BorderStroke(
                0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 20.dp)
                .height(55.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)

        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Appointment",
            )
        }

        // Zurück

        Button(
            onClick = {
                navController.navigate("main")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            border = BorderStroke(
                0.dp, Color.hsv(0.0f, 0.0f, 0.31f, 0.5f, colorSpace = ColorSpaces.Srgb)
            ),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .padding(0.dp, 5.dp, 0.dp, 20.dp)
                .height(55.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)

        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Zurück zum Menü",
            )
        }

        if (appointmentId != null && appointmentId.toInt() != -1) {
            val appointmentIdRes = appointmentId.toInt();

            val appointment =
                state.value.appointments.firstOrNull { it.id == appointmentIdRes }

            if (appointment != null) {
                mTimePickerDialog.updateTime(appointment.date.hour, appointment.date.minute)
                mDatePickerDialog.updateDate(
                    appointment.date.year,
                    appointment.date.monthValue,
                    appointment.date.dayOfMonth
                )
            }
        }
    }
}