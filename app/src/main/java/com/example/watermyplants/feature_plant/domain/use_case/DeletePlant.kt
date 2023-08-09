package com.example.watermyplants.feature_plant.domain.use_case

import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.repository.PlantRepository

class DeletePlant(
    private val repository: PlantRepository
) {
    suspend operator fun invoke(plant: Plant){
        repository.deleteReminder(plant)
    }

}