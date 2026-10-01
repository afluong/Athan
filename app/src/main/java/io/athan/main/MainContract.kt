package io.athan.main

import io.athan.core.model.Location

sealed interface MainUiState {
    data object Loading : MainUiState
    data object NavigateToLocationSearch : MainUiState
    data class NavigateToPrayersSchedule(val location: Location) : MainUiState
}