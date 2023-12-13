package com.wendelledgar.proyectoinventorywendel

import android.app.Application
import com.wendelledgar.proyectoinventorywendel.data.AppContainer
import com.wendelledgar.proyectoinventorywendel.data.AppDataContainer

class RutinasApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
