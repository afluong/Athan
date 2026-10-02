package io.athan.feature.prayersschedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.athan.core.util.formattedStringDate
import io.athan.core.util.toLocalTime
import io.athan.feature.location.domain.repository.LocationRepository
import io.athan.feature.location.presentation.mapper.toDomain
import io.athan.feature.location.presentation.mapper.toUiModel
import io.athan.feature.prayersschedule.data.mapper.findCurrentPrayer
import io.athan.feature.prayersschedule.data.mapper.getPastPrayersTime
import io.athan.feature.prayersschedule.data.mapper.getUpcomingPrayersTimes
import io.athan.feature.prayersschedule.domain.usecase.GetPrayerTimesUseCase
import io.athan.feature.prayersschedule.presentation.mapper.toUiModel
import io.athan.feature.prayersschedule.presentation.mapper.toUiModels
import kotlinx.coroutines.Job
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

    private val _uiState = MutableStateFlow(
        PrayersScheduleUiState(
            isLoading = true,
            selectedDateMillis = System.currentTimeMillis(),
            formattedDate = System.currentTimeMillis().formattedStringDate()
        )
    )
    val uiState: StateFlow<PrayersScheduleUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<PrayersScheduleSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var locationJob: Job? = null

    init {
        observeSelectedLocation()
    }

    fun processIntent(intent: PrayersScheduleIntent) {
        when (intent) {
            is PrayersScheduleIntent.OnDateSelected -> {
                _uiState.update {
                    it.copy(
                        formattedDate = intent.selectedDateMillis.formattedStringDate(),
                        selectedDateMillis = intent.selectedDateMillis,
                        showDatePicker = false
                    )
                }
                loadPrayerTimes(pullToRefresh = false)
            }

            PrayersScheduleIntent.OnLocationSelected,
            PrayersScheduleIntent.OnRetryClicked -> observeSelectedLocation()

            PrayersScheduleIntent.OnCurrentLocationClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(PrayersScheduleSideEffect.OnNavigateToLocation)
                }
            }

            PrayersScheduleIntent.OnDatePickerClicked -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            PrayersScheduleIntent.OnDatePickerDismiss -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            PrayersScheduleIntent.OnPastPrayersClicked -> {
                _uiState.update { it.copy(showPastPrayers = !it.showPastPrayers) }
            }

            PrayersScheduleIntent.OnPullToRefresh -> loadPrayerTimes(pullToRefresh = true)
        }
    }

    private fun observeSelectedLocation() {
        // Annule le Flow précédent pour éviter d'accumuler des abonnements
        locationJob?.cancel()

        _uiState.update { it.copy(isLoading = true, error = null) }

        locationJob = viewModelScope.launch {
            locationRepo.savedLocation.collect { location ->
                if (location != null) {
                    _uiState.update { currentState ->
                        currentState.copy(selectedLocation = location.toUiModel())
                    }
                    loadPrayerTimes(pullToRefresh = false)
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            error = "Can't find location",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun loadPrayerTimes(pullToRefresh: Boolean) {
        val uiLocation = _uiState.value.selectedLocation ?: return
        val selectedDateMillis = _uiState.value.selectedDateMillis

        _uiState.update { currentState ->
            currentState.copy(
                isRefreshing = pullToRefresh,
                isLoading = !pullToRefresh,
                error = null
            )
        }

        viewModelScope.launch {
            getPrayerTimesUseCase(selectedDateMillis, uiLocation.toDomain())
                .onSuccess { prayersSchedule ->
                    val isToday = isSelectedDateToday(selectedDateMillis)
                    val isInPast = isSelectedDateInPast(selectedDateMillis)

                    _uiState.update { currentState ->
                        if (isToday) {
                            val currentTime = Clock.System.now().toEpochMilliseconds().toLocalTime()
                            currentState.copy(
                                upcomingPrayersTimes = prayersSchedule.getUpcomingPrayersTimes(
                                    currentTime
                                ).toUiModels(),
                                pastPrayersTimes = prayersSchedule.getPastPrayersTime(currentTime)
                                    .toUiModels(),
                                currentPrayerTime = prayersSchedule.findCurrentPrayer(currentTime)
                                    ?.toUiModel(),
                                isLoading = false,
                                isRefreshing = false
                            )
                        } else if (isInPast) {
                            currentState.copy(
                                currentPrayerTime = null,
                                pastPrayersTimes = prayersSchedule.toUiModels(),
                                upcomingPrayersTimes = emptyList(),
                                isLoading = false,
                                isRefreshing = false
                            )
                        } else {
                            currentState.copy(
                                currentPrayerTime = null,
                                pastPrayersTimes = emptyList(),
                                upcomingPrayersTimes = prayersSchedule.toUiModels(),
                                isLoading = false,
                                isRefreshing = false
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            error = exception.message ?: "Erreur inconnue",
                            isLoading = false,
                            isRefreshing = false
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

}