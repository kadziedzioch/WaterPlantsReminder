package com.example.watermyplants.feature_plant.presentation.home

import com.example.watermyplants.feature_plant.domain.model.Plant

interface HomeUiState {
    data class Success(val plants: List<Plant>) : HomeUiState
    object NothingFound : HomeUiState
    object Loading: HomeUiState
}