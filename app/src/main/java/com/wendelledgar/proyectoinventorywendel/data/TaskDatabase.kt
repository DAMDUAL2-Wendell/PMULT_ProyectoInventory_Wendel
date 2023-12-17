package com.wendelledgar.proyectoinventorywendel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.runBlocking

@Database(entities = [Task::class], version = 19, exportSchema = false)
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
                    .also {
                        Instance = it

                        // Cargar tareas por defecto
                        runBlocking {
                            tareasPorDefecto(Instance!!.taskDao())
                        }

                    }
            }
        }


        private suspend fun tareasPorDefecto(taskDao: TaskDao) {

            val tareasPrimerDia = listOf<Task>(

                Task(
                    name = "15 min Cardio",
                    description = "Sesión de cardio para mejorar la resistencia cardiovascular y quemar calorías.",
                ),
                Task(
                    name = "Máquina contractora",
                    description = "Ejercicio enfocado en fortalecer los músculos frontales del muslo.",
                ),
                Task(
                    name = "Deltoides posterior",
                    description = "Trabajo de los músculos del deltoides posterior para mejorar la fuerza y flexibilidad.",
                ),
                Task(
                    name = "Aperturas planas",
                    description = "Ejercicio para fortalecer los músculos pectorales, especialmente los cuádriceps.",
                ),
                Task(
                    name = "Elevaciones laterales",
                    description = "Rutina destinada a tonificar y fortalecer los músculos laterales de los glúteos.",
                ),
                Task(
                    name = "Pull over",
                    description = "Ejercicio que trabaja los músculos del pecho y los intercostales.",
                ),
                Task(
                    name = "Tríceps en polea",
                    description = "Enfoque en fortalecer los músculos tríceps.",
                ),
                Task(
                    name = "Press francés",
                    description = "Actividad para desarrollar la musculatura de los tríceps y los hombros.",
                ),
                Task(
                    name = "Abdominales",
                    description = "Ejercicio integral que involucra varios grupos musculares, incluyendo abdomen, piernas y glúteos.",
                ),

            )

            val tareasSegundoDia = listOf<Task>(

                Task(
                    name = "Cuádriceps",
                    description = "Ejercicio enfocado en fortalecer los músculos frontales del muslo.",
                ),
                Task(
                    name = "Femoral sentado",
                    description = "Trabajo de los músculos isquiotibiales para mejorar la fuerza y flexibilidad.",
                ),
                Task(
                    name = "Prensa",
                    description = "Ejercicio para fortalecer los músculos de las piernas, especialmente los cuádriceps.",
                ),
                Task(
                    name = "Gúteos",
                    description = "Rutina destinada a tonificar y fortalecer los músculos de los glúteos.",
                ),
                Task(
                    name = "Aductor",
                    description = "Ejercicio que trabaja los músculos internos del muslo.",
                ),
                Task(
                    name = "Abductor",
                    description = "Enfoque en fortalecer los músculos externos del muslo.",
                ),
                Task(
                    name = "Gemelos en barra",
                    description = "Actividad para desarrollar la musculatura de la pantorrilla.",
                ),
                Task(
                    name = "Sentadillas",
                    description = "Ejercicio integral que involucra varios grupos musculares, incluyendo piernas y glúteos.",
                ),
                Task(
                    name = "15 min Cardio",
                    description = "Sesión de cardio para mejorar la resistencia cardiovascular y quemar calorías.",
                ),

            )

            val tareasTercerDia = listOf<Task>(

                Task(
                    name = "15 min Cardio",
                    description = "Sesión de cardio para mejorar la resistencia cardiovascular y quemar calorías.",
                ),
                Task(
                    name = "Polea al pecho",
                    description = "Ejercicio enfocado en fortalecer los músculos del pecho.",
                ),
                Task(
                    name = "Remo horizontal",
                    description = "Trabajo de los músculos de la espalda para mejorar la fuerza y flexibilidad.",
                ),
                Task(
                    name = "Burpees",
                    description = "Ejercicio para fortalecer los músculos de todo el cuerpo, especialmente los cuádriceps.",
                ),
                Task(
                    name = "Curl bíceps",
                    description = "Rutina destinada a tonificar y fortalecer los músculos de los brazos.",
                ),
                Task(
                    name = "Curl predicador",
                    description = "Ejercicio que trabaja los músculos del bíceps y antebrazos.",
                ),
                Task(
                    name = "Remo con mancuerna",
                    description = "Enfoque en fortalecer los músculos de la espalda y los brazos.",
                ),
                Task(
                    name = "Abdominales y Lumbares",
                    description = "Actividad para desarrollar la musculatura abdominal y lumbar.",
                ),
                Task(
                    name = "10 min Cardio",
                    description = "Sesión de cardio para mantener la resistencia cardiovascular y quemar calorías.",
                ),

            )





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

            taskDao.insertAll(tareasSegundoDia)

        }

    }
}
