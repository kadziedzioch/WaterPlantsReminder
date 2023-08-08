package com.example.watermyplants.feature_plant.presentation.util

sealed class Screen(val route: String){
    object PlantsScreen : Screen("plants_screen")
    object AddEditPlantScreen: Screen("add_edit_plant_screen")
    object CameraScreen: Screen("camera_screen")
}
