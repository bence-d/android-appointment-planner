package com.example.androidappointmentplanner

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Appointment::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppointmentDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
}


