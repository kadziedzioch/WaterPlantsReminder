package com.example.watermyplants.feature_plant.domain.utils

sealed class Resource<T> {
    data class Success<T>(val result: T) : Resource<T>()
    data class Failure<T>(val message: String) : Resource<T>()
}