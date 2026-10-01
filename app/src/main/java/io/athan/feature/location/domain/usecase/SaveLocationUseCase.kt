package io.athan.feature.location.domain.usecase

import io.athan.core.model.Location
import io.athan.feature.location.domain.repository.LocationRepository

class SaveLocationUseCase(private val repository: LocationRepository) {

    suspend operator fun invoke(location: Location) {
        repository.saveSelectedLocation(location)
    }
}