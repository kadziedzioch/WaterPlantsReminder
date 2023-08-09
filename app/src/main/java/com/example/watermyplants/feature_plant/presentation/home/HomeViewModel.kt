package com.example.watermyplants.feature_plant.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermyplants.feature_plant.domain.model.Plant
import com.example.watermyplants.feature_plant.domain.use_case.PlantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantUseCases: PlantUseCases
) : ViewModel() {

    val uiState = plantUseCases
        .getPlants()
        .map {plants->
            if (plants.isEmpty()) HomeUiState.NothingFound
            else HomeUiState.Success(plants)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState.Loading
        )


    fun deletePlant(plant: Plant){
        viewModelScope.launch {
            plantUseCases.deletePlant(plant)
        }
    }

}