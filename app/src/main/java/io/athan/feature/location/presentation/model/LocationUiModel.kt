package io.athan.feature.location.presentation.model

data class LocationUiModel(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lng: Double
)
