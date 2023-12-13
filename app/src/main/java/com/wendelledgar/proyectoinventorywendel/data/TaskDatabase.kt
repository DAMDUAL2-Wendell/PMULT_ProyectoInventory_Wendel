package com.wendelledgar.proyectoinventorywendel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.runBlocking

@Database(entities = [Task::class], version = 15, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var Instance: TaskDatabase? = null
        fun getDataBase(context: Context): TaskDatabase {

            return Instance ?: synchronized(this) {

                Room.databaseBuilder(context, TaskDatabase::class.java, "task_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it

                        // Cargar tareas por defecto
                        runBlocking {
                            tareasPorDefecto(Instance!!.taskDao())
                        }

                    }
            }
        }


        private suspend fun tareasPorDefecto(taskDao: TaskDao) {
            /*
            val task = Task(
                name = "Flexiones",
                description = "Realizar flexiones",
                completado = false,
                totalRepeticiones = 15,
                serie1 = 0,
                serie2 = 0,
                serie3 = 0,
                repeticionesRealizadas = 0,
            )

            taskDao.insert(task)
            */



            val tareasPorDefecto = listOf<Task>(
                Task(
                    name = "Flexiones",
                    description = "Realizar flexiones",
                ),
                Task(
                    name = "Push Up",
                    description = "Realizar Push Up",
                )
            )

            taskDao.insertAll(tareasPorDefecto)



        }

    }
}
