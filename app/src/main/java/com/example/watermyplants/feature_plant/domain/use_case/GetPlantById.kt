package com.example.watermyplants.feature_plant.domain.use_case

import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.repository.PlantRepository

class GetPlantById(
    private val repository: PlantRepository
) {
    suspend operator fun invoke(id:Int): Plant?{
        return repository.getPlantById(id)
    }
}