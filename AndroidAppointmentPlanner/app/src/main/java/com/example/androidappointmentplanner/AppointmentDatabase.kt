package com.example.androidappointmentplanner

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Appointment::class],
    version = 1
)

abstract class AppointmentDatabase : RoomDatabase(){

    //abstract val dao: AppointmentDao
    abstract fun personDao(): AppointmentDao
}