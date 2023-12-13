package com.wendelledgar.proyectoinventorywendel.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(tableName = "tasks",indices = [Index(value = ["name"], unique = true)])
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    @ColumnInfo(name = "repeticiones_realizadas")
    val repeticionesRealizadas: Int = 0,
    @ColumnInfo(name = "total_repeticiones")
    val totalRepeticiones: Int = 15,
    val serie1: Int = 0,
    val serie2: Int = 0,
    val serie3: Int = 0,
    val completado: Boolean = false,
)

