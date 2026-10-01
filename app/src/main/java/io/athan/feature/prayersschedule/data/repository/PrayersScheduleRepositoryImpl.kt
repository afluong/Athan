package io.athan.feature.prayersschedule.data.repository

import io.athan.core.model.Location
import io.athan.core.model.PrayerTime
import io.athan.feature.prayersschedule.data.mapper.toDomain
import io.athan.feature.prayersschedule.data.remote.AladhanApiService
import io.athan.feature.prayersschedule.domain.repository.PrayersScheduleRepository
import java.net.UnknownHostException

class PrayersScheduleRepositoryImpl(
    private val remote: AladhanApiService
) : PrayersScheduleRepository {
    override suspend fun loadPrayersTimeForLocation(
        dateMillis: Long,
        location: Location
    ): Result<List<PrayerTime>> {
        val timeStampSeconds = dateMillis / 1000

        return try {
            val response = remote.getPrayerTimes(timeStampSeconds, location.lat, location.lng)
            val formattedResponse = response.data.toDomain()
            return Result.success(formattedResponse)
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> Result.failure(UnknownHostException("No internet connection"))
                else -> Result.failure(e)
            }
        }
    }
}