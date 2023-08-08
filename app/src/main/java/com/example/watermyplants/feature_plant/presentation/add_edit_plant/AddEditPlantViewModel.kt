package com.example.watermyplants.feature_plant.presentation.add_edit_plant

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermyplants.feature_plant.domain.model.InvalidPlantException
import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.use_case.PlantUseCases
import com.example.watermyplants.feature_plant.domain.utils.TimePeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AddEditPlantViewModel @Inject constructor(
    private val plantUseCases: PlantUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _plantName = mutableStateOf(PlantTextFieldState())
    val plantName : State<PlantTextFieldState> = _plantName

    private val _plantDescription = mutableStateOf(PlantTextFieldState())
    val plantDescription : State<PlantTextFieldState> = _plantDescription

    private val _dropDownMenuState = mutableStateOf(DropDownMenuState())
    val dropDownMenuState :State<DropDownMenuState> = _dropDownMenuState

    private val _duration = mutableStateOf(PlantTextFieldState())
    val duration : State<PlantTextFieldState> = _duration

    private val _imgUri = mutableStateOf("")
    val imgUri : State<String> = _imgUri

    private val channel =  Channel<UiEvent>()
    val eventFlow = channel.receiveAsFlow()


    private var currentPlantId: Int? = null
    private var currentWorkId: UUID? = null

    init {
        savedStateHandle.get<Int>("plantId")?.let {plantId ->
            if(plantId !=-1){
                viewModelScope.launch {
                    plantUseCases.getPlantById(plantId)?.also {plant ->
                        currentPlantId = plant.id
                        currentWorkId = plant.workId
                        _plantDescription.value = plantDescription.value.copy(
                            text = plant.description
                        )
                        _plantName.value = plantName.value.copy(
                            text = plant.name
                        )
                        _dropDownMenuState.value = dropDownMenuState.value.copy(
                            selectedTimeUnit = TimePeriod.valueOf(plant.unit.name)
                        )
                        _duration.value = duration.value.copy(
                            text = plant.duration.toString()
                        )
                        _imgUri.value = plant.imageUri.toString()
                    }
                }
            }
        }
    }


    fun onEvent(event: AddEditPlantEvent) {
        when(event){
            is AddEditPlantEvent.DescriptionEntered -> {
                _plantDescription.value = plantDescription.value.copy(
                    text = event.description
                )
            }
            is AddEditPlantEvent.DurationEntered -> {
                _duration.value = duration.value.copy(
                    text = event.duration.toString()
                )
            }
            is AddEditPlantEvent.NameEntered -> {
                _plantName.value = plantName.value.copy(
                    text = event.name
                )
            }
            is AddEditPlantEvent.PhotoPicked -> {
                _imgUri.value = event.uri
            }
            is AddEditPlantEvent.PlantSaved -> {

                viewModelScope.launch {
                    try {
                        val dur = duration.value.text
                        plantUseCases.insertPlant(
                            Plant(
                                id = currentPlantId,
                                name = plantName.value.text,
                                description =plantDescription.value.text,
                                duration = if(dur.isNotEmpty()) dur.toLong() else 0,
                                unit = dropDownMenuState.value.selectedTimeUnit.timeUnit,
                                imageUri = Uri.parse(imgUri.value),
                                workId = currentWorkId
                            )
                        )
                        channel.send(UiEvent.SavePlant)
                    }
                    catch(e: InvalidPlantException){
                        channel.send(
                            UiEvent.ShowSnackBar(
                                e.message
                            )
                        )
                    }
                }
            }
            is AddEditPlantEvent.DropDownMenuExpandStateChanged ->{
                _dropDownMenuState.value = dropDownMenuState.value.copy(
                    isExpanded = !dropDownMenuState.value.isExpanded
                )
            }
            is AddEditPlantEvent.TimeUnitEntered -> {
                _dropDownMenuState.value = dropDownMenuState.value.copy(
                    selectedTimeUnit = event.timeUnit,
                    isExpanded = false
                )
            }
        }

    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SavePlant: UiEvent()
    }

}