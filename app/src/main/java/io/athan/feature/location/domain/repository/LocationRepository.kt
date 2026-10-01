package io.athan.feature.location.domain.repository

import io.athan.core.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val savedLocation: Flow<Location?>

    suspend fun searchLocationFor(query: String): Result<List<Location>>
    suspend fun saveSelectedLocation(location: Location)
}