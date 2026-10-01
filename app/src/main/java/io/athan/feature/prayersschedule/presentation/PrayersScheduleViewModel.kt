package io.athan.feature.prayersschedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.athan.feature.location.domain.repository.LocationRepository
import io.athan.feature.location.presentation.mapper.toDomain
import io.athan.feature.location.presentation.mapper.toUiModel
import io.athan.feature.prayersschedule.data.mapper.findCurrentPrayer
import io.athan.feature.prayersschedule.data.mapper.getPastPrayersTime
import io.athan.feature.prayersschedule.data.mapper.getUpcomingPrayersTimes
import io.athan.feature.prayersschedule.domain.usecase.GetPrayerTimesUseCase
import io.athan.feature.prayersschedule.presentation.mapper.toUiModel
import io.athan.feature.prayersschedule.presentation.mapper.toUiModels
import io.athan.core.util.formattedStringDate
import io.athan.core.util.toLocalTime
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class PrayersScheduleViewModel(
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase,
    private val locationRepo: LocationRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(PrayersScheduleUiState(isLoading = true))
    val uiState: StateFlow<PrayersScheduleUiState> = _uiState.asStateFlow()

    private var _sideEffect = Channel<PrayersScheduleSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeSelectedLocation()
        updateDate(System.currentTimeMillis())
    }

    fun processIntent(intent: PrayersScheduleIntent) {
        when (intent) {
            is PrayersScheduleIntent.onDateSelected -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        formattedDate = intent.selectedDateMillis.formattedStringDate(),
                        selectedDateMillis = intent.selectedDateMillis,
                        showDatePicker = false,
                        isLoading = true
                    )
                }
                loadPrayerTimesForSelectedDate()
            }

            PrayersScheduleIntent.onLocationSelected -> observeSelectedLocation()

            PrayersScheduleIntent.onCurrentLocationClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(PrayersScheduleSideEffect.onNavigateToLocation)
                }
            }

            PrayersScheduleIntent.onDatePickerClicked -> {
                _uiState.update { currentState ->
                    currentState.copy(showDatePicker = true)
                }
            }

            PrayersScheduleIntent.onDatePickerDismiss -> {
                _uiState.update { currentState ->
                    currentState.copy(showDatePicker = false)
                }
            }

            PrayersScheduleIntent.onPastPrayersClicked -> {
                _uiState.update { currentState ->
                    currentState.copy(showPastPrayers = !currentState.showPastPrayers)
                }
            }

            PrayersScheduleIntent.onRefresh -> {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = true)
                }
                observeSelectedLocation()
            }
        }
    }

    private fun observeSelectedLocation() {
        viewModelScope.launch {
            locationRepo.savedLocation.collect { location ->
                if (location != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            selectedLocation = location.toUiModel()
                        )
                    }
                    loadPrayerTimesForSelectedDate()
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            error = "Can't find location",
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun loadPrayerTimesForSelectedDate() {
        viewModelScope.launch {
            val uiLocation = uiState.value.selectedLocation ?: return@launch
            val selectedDateMillis = _uiState.value.selectedDateMillis

            getPrayerTimesUseCase(selectedDateMillis, uiLocation.toDomain())
                .onSuccess { prayersSchedule ->
                    val isToday = isSelectedDateToday(selectedDateMillis)
                    val isInPast = isSelectedDateInPast(selectedDateMillis)

                    if (isToday) {
                        val currentTime = Clock.System.now().toEpochMilliseconds().toLocalTime()
                        _uiState.update { currentState ->
                            currentState.copy(
                                selectedLocation = uiLocation,
                                upcomingPrayersTimes = prayersSchedule.getUpcomingPrayersTimes(
                                    currentTime
                                ).toUiModels(),
                                pastPrayersTimes = prayersSchedule.getPastPrayersTime(
                                    currentTime
                                ).toUiModels(),
                                currentPrayerTime = prayersSchedule.findCurrentPrayer(
                                    currentTime
                                )?.toUiModel(),
                                isLoading = false,
                                error = null
                            )
                        }
                    } else if (isInPast) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                selectedLocation = uiLocation,
                                selectedDateMillis = selectedDateMillis,
                                currentPrayerTime = null,
                                pastPrayersTimes = prayersSchedule.toUiModels(),
                                upcomingPrayersTimes = emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                selectedLocation = uiLocation,
                                selectedDateMillis = selectedDateMillis,
                                currentPrayerTime = null,
                                pastPrayersTimes = emptyList(),
                                upcomingPrayersTimes = prayersSchedule.toUiModels(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            error = exception.message,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun isSelectedDateToday(dateMillis: Long): Boolean {
        val timeZone = TimeZone.currentSystemDefault()
        val selectedDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(timeZone).date
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        return selectedDate == today
    }

    private fun isSelectedDateInPast(dateMillis: Long): Boolean {
        val timeZone = TimeZone.currentSystemDefault()
        val selectedDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(timeZone).date
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        return selectedDate < today
    }

    private fun updateDate(dateMillis: Long) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    formattedDate = dateMillis.formattedStringDate(),
                    selectedDateMillis = dateMillis
                )
            }
        }
    }
}