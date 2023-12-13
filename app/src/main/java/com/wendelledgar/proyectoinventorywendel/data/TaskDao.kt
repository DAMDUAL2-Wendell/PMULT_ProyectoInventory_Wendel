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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(taskList: List<Task>)

    @Update
    suspend fun update(task: Task)
    @Delete
    suspend fun delete(task: Task)
    @Query("SELECT * from tasks WHERE id = :id")
    fun getTask(id: Int): Flow<Task>
    @Query("SELECT * from tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>



}