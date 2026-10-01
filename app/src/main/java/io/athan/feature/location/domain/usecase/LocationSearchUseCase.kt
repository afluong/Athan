package io.athan.feature.location.domain.usecase

import io.athan.core.model.Location
import io.athan.feature.location.domain.repository.LocationRepository

class LocationSearchUseCase(private val repository: LocationRepository) {

    suspend operator fun invoke(query: String): Result<List<Location>> =
        repository.searchLocationFor(query)
}