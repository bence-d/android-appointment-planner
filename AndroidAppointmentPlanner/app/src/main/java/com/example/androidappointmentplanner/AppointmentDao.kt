package com.example.androidappointmentplanner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Upsert
    fun save(appointment: Appointment)

    @Delete
    fun delete(appointment: Appointment)

    @Query("SELECT * from Appointment ORDER BY title ASC")
    fun findAll(): Flow<List<Appointment>>
}