package io.athan.feature.location.data.repository

import io.athan.core.model.Location
import io.athan.feature.location.data.local.dao.LocationDao
import io.athan.feature.location.data.mapper.toDomain
import io.athan.feature.location.data.mapper.toEntity
import io.athan.feature.location.data.remote.api.PhotonApiService
import io.athan.feature.location.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(
    private val remote: PhotonApiService,
    private val local: LocationDao
) : LocationRepository {

    override val savedLocation: Flow<Location?> =
        local.getSavedLocation().map { entity -> entity?.toDomain() }

    override suspend fun saveSelectedLocation(location: Location) {
        local.saveLocation(location.toEntity())
    }

    override suspend fun searchLocationFor(query: String): Result<List<Location>> {
        try {
            val response = remote.getPlacePredictions(query)
            val predictions = response.features.map { prediction ->
                prediction.toDomain()
            }
            return Result.success(predictions)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}