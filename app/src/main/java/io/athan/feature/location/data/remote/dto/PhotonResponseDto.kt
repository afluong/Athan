package io.athan.feature.location.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotonResponseDto(
    @SerialName("features") val features: List<PhotonFeatureDto> = emptyList()
)

@Serializable
data class PhotonFeatureDto(
    @SerialName("properties") val properties: PhotonPropertiesDto,
    @SerialName("geometry") val geometry: PhotonGeometryDto
)

@Serializable
data class PhotonPropertiesDto(
    @SerialName("osm_id") val osmId: Long,
    @SerialName("name") val name: String,
    @SerialName("country") val country: String,
)

@Serializable
data class PhotonGeometryDto(
    @SerialName("coordinates") val coordinates: List<Double> = emptyList()
)
