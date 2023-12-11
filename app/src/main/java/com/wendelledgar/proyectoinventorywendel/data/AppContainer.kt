/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wendelledgar.proyectoinventorywendel.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val tasksRepository: TasksRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineTasksRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [TasksRepository]
     */
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(InventoryDatabase.getDataBase(context).itemDao())
        // Maxia de Room: cando se chama ao metodo getDatabase a Instance que se devolve e un
        // obxecto InventoryDatabase que xa ten implementado o metodo itemDao() que apunta a
        // interfaz creada coa notacion @Dao. Esto faise a través de 'Room.databaseBuilder'
    }
}
