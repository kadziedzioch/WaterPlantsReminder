package com.example.watermyplants.feature_plant.presentation.add_edit_plant

import com.example.watermyplants.feature_plant.domain.utils.TimePeriod

data class DropDownMenuState(
    val selectedTimeUnit: TimePeriod = TimePeriod.DAYS,
    val isExpanded: Boolean = false
)