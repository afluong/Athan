package io.athan.feature.prayersschedule.domain.repository

import io.athan.core.model.Location
import io.athan.core.model.PrayerTime

interface PrayersScheduleRepository {

    suspend fun loadPrayersTimeForLocation(dateMillis: Long, location: Location): Result<List<PrayerTime>>
}