package com.wendelledgar.proyectoinventorywendel.data

import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getAllItemsStream(): Flow<List<Task>>
    fun getItemStream(id: Int): Flow<Task?>
    suspend fun insertItem(task: Task)
    suspend fun insertListTasks(list: List<Task>)
    suspend fun deleteItem(task: Task)
    suspend fun updateItem(task: Task)

}

