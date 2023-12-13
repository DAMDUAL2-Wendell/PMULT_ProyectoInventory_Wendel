package com.wendelledgar.proyectoinventorywendel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A clase da bbdd con un singleton Instance
 */
@Database(entities = [Task::class], version = 5, exportSchema = false)
// Cada vez que se cambie o esquema aumentar en 1 a version. exportSchema para copias de seguridade
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): TaskDao

    // Solo se necesita unha instancia de RoomDataBase, logo usaremos un singleton
    companion object {

        // Esta variable conserva unha referencia a bbdd cando se crea, de forma que solo exista
        // unha (e un recurso costos de crear e manter). 'Volatile' e para que o valor da variable
        // non se almacene en cache. Asi Instance esta sempre actualizado para todos os subprocesos
        @Volatile
        private var Instance: InventoryDatabase? = null

        // E posible que varios subprocesos soliciten unha instancia da bbdd ao mesmo tempo.
        // O codigo dentro do bloque synchronized e para que solo un subproceso poida ingresar
        // a este bloque a vez. Esto garante que a bbdd solo se inicialice unha vez
        fun getDataBase(context: Context): InventoryDatabase {

            // E dicir, este metodo devolve a instancia da bbdd se existe e senon a incicializa
            // con garantia de que solo se faga unha vez
            return Instance ?: synchronized(this) {
                // No argumento lock tense que especificar o obxecto onde esta o bloque de codigo,
                // neste caso InventoryDatabase (this)
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it}
            }
        }
    }
}
