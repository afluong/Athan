package io.athan.feature.prayersschedule.domain.usecase

import io.athan.core.model.Location
import io.athan.core.model.PrayerTime
import io.athan.feature.prayersschedule.domain.repository.PrayersScheduleRepository

class GetPrayerTimesUseCase(private val repository: PrayersScheduleRepository) {
    suspend operator fun invoke(
        selectedDateMillis: Long,
        location: Location
    ): Result<List<PrayerTime>> =
        repository.loadPrayersTimeForLocation(selectedDateMillis, location)
            .onSuccess { prayerTimes ->
                Result.success(prayerTimes)
            }
            .onFailure { exception ->
                Result.failure<Exception>(exception)
            }
}