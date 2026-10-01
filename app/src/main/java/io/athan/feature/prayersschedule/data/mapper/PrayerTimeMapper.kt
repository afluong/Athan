package io.athan.feature.prayersschedule.data.mapper

import io.athan.core.model.PrayerTime
import io.athan.feature.prayersschedule.data.remote.dto.AladhanDataDto
import kotlinx.datetime.LocalTime

fun AladhanDataDto.toDomain(): List<PrayerTime> {
    val filteredPrayers = listOf("Sunrise", "Dhuhr", "Asr", "Maghrib", "Isha")

    return this.timings
        .filterKeys { prayerName -> filteredPrayers.contains(prayerName) }
        .mapNotNull { (key, value) ->
            PrayerTime(
                name = key,
                time = LocalTime.parse(value)
            )
        }
}

fun List<PrayerTime>.getPastPrayersTime(currentTime: LocalTime) : List<PrayerTime> {
    val currentPrayer = this.findCurrentPrayer(currentTime)
    val currentIndex = this.indexOf(currentPrayer)

    return this.subList(0, currentIndex)
}

fun List<PrayerTime>.getUpcomingPrayersTimes(currentTime: LocalTime) : List<PrayerTime> {
    val currentPrayer = this.findCurrentPrayer(currentTime)
    val currentIndex = this.indexOf(currentPrayer)

    return this.subList(currentIndex + 1, this.size)
}

fun List<PrayerTime>.findCurrentPrayer(currentTime: LocalTime): PrayerTime? =
    this.lastOrNull { prayerTime ->
        prayerTime.time.toMinutesOfDay() <= currentTime.toMinutesOfDay()
    }


fun LocalTime.toMinutesOfDay() = this.hour * 60 + this.minute
