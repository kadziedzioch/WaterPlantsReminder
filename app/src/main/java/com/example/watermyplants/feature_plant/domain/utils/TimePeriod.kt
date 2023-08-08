package com.example.watermyplants.feature_plant.domain.utils

import java.util.concurrent.TimeUnit

enum class TimePeriod(val timeUnit: TimeUnit) {
    HOURS(TimeUnit.HOURS), DAYS(TimeUnit.DAYS), SECONDS(TimeUnit.SECONDS)
}