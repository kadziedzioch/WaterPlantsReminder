package com.example.watermyplants.feature_plant.domain.use_case

data class PlantUseCases (
    val deletePlant: DeletePlant,
    val getPlantById: GetPlantById,
    val getPlants: GetPlants,
    val insertPlant: InsertPlant
    )