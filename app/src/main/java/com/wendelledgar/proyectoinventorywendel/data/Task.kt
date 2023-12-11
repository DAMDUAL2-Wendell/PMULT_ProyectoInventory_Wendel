package com.wendelledgar.proyectoinventorywendel.data

import androidx.room.Entity
import androidx.room.PrimaryKey


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
    val completado: Boolean = false
)
