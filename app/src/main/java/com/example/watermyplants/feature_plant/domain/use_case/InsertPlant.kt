package com.example.watermyplants.feature_plant.domain.use_case

import com.example.watermyplants.feature_plant.domain.model.InvalidPlantException
import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.repository.PlantRepository

class InsertPlant(
    private val repository: PlantRepository
) {

    @Throws(InvalidPlantException::class)
    suspend operator fun invoke(plant: Plant){

        if(plant.name.isBlank()){
            throw InvalidPlantException("The name cannot be blank")
        }
        if(plant.description.isBlank()){
            throw InvalidPlantException("The description cannot be blank")
        }
        if(plant.duration<= 0){
            throw InvalidPlantException("The duration cannot be less or equal to 0")
        }
        repository.scheduleReminder(plant)
    }

}