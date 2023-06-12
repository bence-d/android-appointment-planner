package com.example.androidappointmentplanner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Insert
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Insert
    suspend fun save(appointment: Appointment)

    @Update
    suspend fun update(appointment: Appointment)

    @Delete
    suspend fun delete(appointment: Appointment)

    @Query("SELECT * from Appointment ORDER BY title ASC")
    fun findAll(): Flow<List<Appointment>>

    @Query("SELECT * FROM Appointment WHERE id = :id")
    fun findById(id: Int): Flow<Appointment>

    @Query("SELECT * FROM Appointment ORDER BY date ASC")
    fun findAllByDate(): Flow<List<Appointment>>

    @Query("SELECT * FROM Appointment ORDER BY category ASC, date ASC")
    fun findAllByCategory(): Flow<List<Appointment>>
}
