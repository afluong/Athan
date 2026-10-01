package io.athan.feature.location.domain.usecase

import io.athan.core.model.Location
import io.athan.feature.location.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetSavedLocationUseCase(private val repository: LocationRepository) {

    operator fun invoke(): Flow<Location?> = repository.savedLocation
}