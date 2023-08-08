package com.example.watermyplants.feature_plant.domain.repository

import com.example.watermyplants.feature_plant.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantRepository {

    fun getPlants(): Flow<List<Plant>>

    suspend fun getPlantById(id: Int): Plant?

    suspend fun scheduleReminder(plant: Plant)

    suspend fun deleteReminder(plant: Plant)
}