package io.athan.feature.prayersschedule.presentation.mapper

import io.athan.core.model.PrayerTime
import io.athan.feature.prayersschedule.presentation.model.PrayerTimeUiModel
import kotlinx.datetime.LocalTime

fun PrayerTime.toUiModel(): PrayerTimeUiModel =
    PrayerTimeUiModel(
        name = this.name,
        time = this.time.toFormatted2DigitsString()
    )

fun List<PrayerTime>.toUiModels(): List<PrayerTimeUiModel> =
    this.map { prayerTime ->
        PrayerTimeUiModel(
            name = prayerTime.name,
            time = prayerTime.time.toFormatted2DigitsString()
        )
    }

private fun LocalTime.toFormatted2DigitsString(): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}