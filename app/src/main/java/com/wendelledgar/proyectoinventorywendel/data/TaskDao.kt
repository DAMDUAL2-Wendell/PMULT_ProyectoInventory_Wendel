package com.wendelledgar.proyectoinventorywendel.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Nesta app solo insertamos dende un lugar
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * from tasks WHERE id = :id")
    fun getItem(id: Int): Flow<Task> // Como devolve Flow non hai que chamala detro do scope da corrutina

    @Query("SELECT * from tasks ORDER BY name ASC")
    fun getAllItems(): Flow<List<Task>>



}