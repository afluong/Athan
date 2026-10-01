package io.athan.feature.location.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GooglePlacesResponseDto(
    @SerialName("error_message") val errorMessage: String,
    @SerialName("status") val status: String,
    @SerialName("predictions") val predictions: List<GooglePlacePredictionsDto>
)

@Serializable
data class GooglePlacePredictionsDto(
    @SerialName("place_id") val placeId: Int,
    @SerialName("description") val description: String
)
