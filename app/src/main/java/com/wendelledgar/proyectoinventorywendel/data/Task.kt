package com.wendelledgar.proyectoinventorywendel.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Entity data class represents a single row in the database.
 */

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val seriesRealizadas: Int,
    val quantity: Int,
    val serie1: Int,
    val serie2: Int,
    val serie3: Int,
    val completado: Boolean = false,
)

object seriesPorDefecto{
    val numeroRepeticiones: Int = 15
}

