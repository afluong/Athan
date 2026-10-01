package io.athan.feature.location.data.mapper

import io.athan.core.model.Location
import io.athan.feature.location.data.local.entity.LocationEntity
import io.athan.feature.location.data.remote.dto.PhotonFeatureDto

fun PhotonFeatureDto.toDomain(): Location {
    val lat = this.geometry.coordinates[1]
    val lng = this.geometry.coordinates[0]

    return Location(
        id = this.properties.osmId,
        name = this.properties.name,
        country = this.properties.country,
        lat = lat,
        lng = lng
    )
}

fun Location.toEntity(): LocationEntity =
    LocationEntity(
        name = this.name,
        country = this.country,
        latitude = this.lat,
        longitude = this.lng
    )

fun LocationEntity.toDomain(): Location =
    Location(
        id = this.id.toLong(),
        name = this.name,
        country = this.country,
        lat = this.latitude,
        lng = this.longitude,
    )