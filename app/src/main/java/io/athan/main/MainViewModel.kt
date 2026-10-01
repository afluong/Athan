package io.athan.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.athan.feature.location.domain.usecase.GetSavedLocationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    getSavedLocation: GetSavedLocationUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState?> = getSavedLocation().map { location ->
        if (location == null) {
            MainUiState.NavigateToLocationSearch
        } else {
            MainUiState.NavigateToPrayersSchedule(location)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = MainUiState.Loading
        )
}
