package io.athan.core.model

data class Location(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)