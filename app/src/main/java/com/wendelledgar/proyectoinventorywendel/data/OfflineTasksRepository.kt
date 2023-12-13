package com.wendelledgar.proyectoinventorywendel.data

import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(private val taskDao: TaskDao) : TasksRepository {
    override fun getAllItemsStream(): Flow<List<Task>> = taskDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Task?> = taskDao.getItem(id)

    override suspend fun insertItem(task: Task) = taskDao.insert(task)

    override suspend fun insertListTasks(list: List<Task>) = taskDao.insertAll(list)

    override suspend fun deleteItem(task: Task) = taskDao.delete(task)

    override suspend fun updateItem(task: Task) = taskDao.update(task)

}
