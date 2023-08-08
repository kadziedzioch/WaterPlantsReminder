package com.example.watermyplants.feature_plant.domain.use_case

import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow


class GetPlants(
    private val repository: PlantRepository
) {
    operator fun invoke() : Flow<List<Plant>> {
        return repository.getPlants()
    }
}