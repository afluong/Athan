package io.athan.feature.location.presentation

import io.athan.core.model.Location

sealed interface LocationSearchUiState {
    val query: String
    val hasSavedLocation: Boolean

    data class Idle(
        override val query: String = "",
        override val hasSavedLocation: Boolean = false
    ) : LocationSearchUiState

    data class Loading(
        override val query: String = "",
        override val hasSavedLocation: Boolean = false
    ) :
        LocationSearchUiState

    data class Empty(
        override val query: String = "",
        override val hasSavedLocation: Boolean = false
    ) : LocationSearchUiState

    data class Success(
        override val query: String,
        override val hasSavedLocation: Boolean = false,
        val predictions: List<Location>
    ) : LocationSearchUiState

    data class Error(
        override val query: String,
        override val hasSavedLocation: Boolean = false,
        val message: String
    ) : LocationSearchUiState
}

sealed interface LocationSearchIntent {
    data class OnQueryChanged(val query: String) : LocationSearchIntent
    data class OnLocationSelected(val location: Location) : LocationSearchIntent
    data object OnBackClicked : LocationSearchIntent
}

sealed interface LocationSearchSideEffect {
    data class OnNavigateToSchedule(val selectedLocation: Location) : LocationSearchSideEffect
    data object OnNavigateBack : LocationSearchSideEffect
}