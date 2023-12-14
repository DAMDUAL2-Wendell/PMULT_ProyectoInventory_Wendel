package com.wendelledgar.proyectoinventorywendel.data

import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getAllTasksStream(): Flow<List<Task>>
    fun getTaskStream(id: Int): Flow<Task?>
    fun getTaskStreamByName(name: String): Flow<Task?>
    suspend fun insertTask(task: Task)
    suspend fun insertListTasks(list: List<Task>)
    suspend fun deletetask(task: Task)
    suspend fun updateTask(task: Task)


}

