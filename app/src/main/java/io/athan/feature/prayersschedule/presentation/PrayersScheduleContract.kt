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
    val isRefreshing: Boolean = false,
    val showDatePicker: Boolean = false,
    val showPastPrayers: Boolean = false,
    val error: String? = null
)

sealed interface PrayersScheduleIntent {
    data object OnDatePickerClicked : PrayersScheduleIntent
    data object OnDatePickerDismiss : PrayersScheduleIntent
    data object OnCurrentLocationClicked : PrayersScheduleIntent
    data class OnDateSelected(val selectedDateMillis: Long) : PrayersScheduleIntent
    data object OnLocationSelected : PrayersScheduleIntent
    data object OnPastPrayersClicked : PrayersScheduleIntent
    data object OnRetryClicked : PrayersScheduleIntent
    data object OnPullToRefresh : PrayersScheduleIntent
}

sealed interface PrayersScheduleSideEffect {
    data object OnNavigateToLocation : PrayersScheduleSideEffect
}

