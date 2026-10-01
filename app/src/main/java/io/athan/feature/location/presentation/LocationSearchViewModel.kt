package io.athan.feature.location.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.athan.feature.location.domain.usecase.GetSavedLocationUseCase
import io.athan.feature.location.domain.usecase.LocationSearchUseCase
import io.athan.feature.location.domain.usecase.SaveLocationUseCase
import io.athan.feature.location.presentation.LocationSearchSideEffect.OnNavigateBack
import io.athan.feature.location.presentation.LocationSearchSideEffect.OnNavigateToSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class LocationSearchViewModel(
    private val getLocationForSearch: LocationSearchUseCase,
    private val saveSelectedLocation: SaveLocationUseCase,
    getSavedLocationUseCase: GetSavedLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LocationSearchUiState>(LocationSearchUiState.Loading())
    val uiState: StateFlow<LocationSearchUiState> = combine(
        getSavedLocationUseCase(),
        _uiState
    ) { savedLocation, currentState ->
        val hasSaved = savedLocation != null
        currentState.withSavedLocation(hasSaved)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LocationSearchUiState.Idle(hasSavedLocation = false)
    )

    private val _sideEffect = Channel<LocationSearchSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

    fun processIntent(intent: LocationSearchIntent) {
        when (intent) {
            is LocationSearchIntent.OnQueryChanged -> {
                _searchQuery.value = intent.query

                _uiState.update { currentState ->
                    when (currentState) {
                        is LocationSearchUiState.Idle -> currentState.copy(query = intent.query)
                        is LocationSearchUiState.Loading -> currentState.copy(query = intent.query)
                        is LocationSearchUiState.Success -> currentState.copy(query = intent.query)
                        is LocationSearchUiState.Empty -> currentState.copy(query = intent.query)
                        is LocationSearchUiState.Error -> currentState.copy(query = intent.query)
                    }
                }
            }

            is LocationSearchIntent.OnLocationSelected -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveSelectedLocation(intent.location)
                    _sideEffect.send(
                        OnNavigateToSchedule(intent.location)
                    )
                }
            }

            LocationSearchIntent.OnBackClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(OnNavigateBack)
                }
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L.milliseconds)
                .collectLatest { query ->
                    if (query.isBlank() || query.length <= MINIMUM_SEARCH_LENGTH) {
                        _uiState.update { LocationSearchUiState.Idle(query = query) }
                        return@collectLatest
                    }

                    _uiState.value = LocationSearchUiState.Loading(query = query)

                    getLocationForSearch(query)
                        .onSuccess { locations ->
                            _uiState.value = if (locations.isEmpty()) {
                                LocationSearchUiState.Empty(query = query)
                                return@collectLatest
                            } else {
                                LocationSearchUiState.Success(
                                    query = query,
                                    predictions = locations
                                )
                            }
                        }
                        .onFailure { error ->
                            _uiState.value =
                                LocationSearchUiState.Error(
                                    query = query,
                                    message = error.toString()
                                )
                            return@collectLatest
                        }
                }
        }
    }

    private fun LocationSearchUiState.withSavedLocation(hasSaved: Boolean): LocationSearchUiState {
        return when (this) {
            is LocationSearchUiState.Idle -> copy(hasSavedLocation = hasSaved)
            is LocationSearchUiState.Loading -> copy(hasSavedLocation = hasSaved)
            is LocationSearchUiState.Empty -> copy(hasSavedLocation = hasSaved)
            is LocationSearchUiState.Success -> copy(hasSavedLocation = hasSaved)
            is LocationSearchUiState.Error -> copy(hasSavedLocation = hasSaved)
        }
    }


    companion object {
        const val MINIMUM_SEARCH_LENGTH = 2
    }
}