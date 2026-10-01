package io.athan.feature.prayersschedule.presentation

import io.athan.feature.location.presentation.model.LocationUiModel
import io.athan.feature.prayersschedule.presentation.model.PrayerTimeUiModel
import io.athan.core.util.formattedStringDate
import kotlin.time.Clock


data class PrayersScheduleUiState(
    val selectedLocation: LocationUiModel? = null,
    val formattedDate: String = Clock.System.now().toEpochMilliseconds().formattedStringDate(),
    val selectedDateMillis: Long = Clock.System.now().toEpochMilliseconds(),
    val upcomingPrayersTimes: List<PrayerTimeUiModel> = emptyList(),
    val pastPrayersTimes: List<PrayerTimeUiModel> = emptyList(),
    val currentPrayerTime: PrayerTimeUiModel? = null,
    val isLoading: Boolean = false,
    val showDatePicker: Boolean = false,
    val showPastPrayers: Boolean = false,
    val error: String? = null
)

sealed interface PrayersScheduleIntent {
    data object onDatePickerClicked : PrayersScheduleIntent
    data object onDatePickerDismiss : PrayersScheduleIntent
    data object onCurrentLocationClicked : PrayersScheduleIntent
    data class onDateSelected(val selectedDateMillis: Long) : PrayersScheduleIntent
    data object onLocationSelected : PrayersScheduleIntent
    data object onPastPrayersClicked : PrayersScheduleIntent
    data object onRefresh : PrayersScheduleIntent
}

sealed interface PrayersScheduleSideEffect {
    data object onNavigateToLocation : PrayersScheduleSideEffect
}

