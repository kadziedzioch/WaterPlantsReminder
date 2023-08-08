package com.example.watermyplants.feature_plant.presentation.add_edit_plant

import android.net.Uri
import com.example.watermyplants.feature_plant.domain.utils.TimePeriod
import java.time.Duration
import java.util.concurrent.TimeUnit

sealed class AddEditPlantEvent{
    data class NameEntered(val name: String): AddEditPlantEvent()
    data class DescriptionEntered(val description: String): AddEditPlantEvent()
    data class PhotoPicked(val uri: String): AddEditPlantEvent()
    data class TimeUnitEntered(val timeUnit: TimePeriod): AddEditPlantEvent()
    object DropDownMenuExpandStateChanged : AddEditPlantEvent()
    data class DurationEntered(val duration: String): AddEditPlantEvent()
    object PlantSaved : AddEditPlantEvent()
}
