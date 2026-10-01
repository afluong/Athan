package io.athan.feature.location.presentation.mapper

import io.athan.core.model.Location
import io.athan.feature.location.presentation.model.LocationUiModel

fun LocationUiModel.toDomain(): Location =
    Location(
        id = this.id,
        name = this.name,
        country = this.country,
        lat = this.lat,
        lng = this.lng
    )

fun Location.toUiModel(): LocationUiModel =
    LocationUiModel(
        id = this.id,
        name = this.name,
        country = this.country,
        lat = this.lat,
        lng = this.lng,
    )