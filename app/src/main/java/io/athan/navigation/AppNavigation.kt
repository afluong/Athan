package io.athan.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.athan.feature.location.presentation.LocationSearchScreen
import io.athan.feature.prayersschedule.presentation.PrayersScheduleScreen
import io.athan.main.MainUiState
import io.athan.main.MainViewModel
import io.athan.navigation.Destination.LOCATION_SEARCH
import io.athan.navigation.Destination.PRAYERS_SCHEDULE
import org.koin.androidx.compose.koinViewModel

object Destination {
    const val LOCATION_SEARCH = "location_search"
    const val PRAYERS_SCHEDULE = "prayers_schedule"
}

@Composable
fun AppNavigation(viewModel: MainViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MainUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MainUiState.NavigateToLocationSearch,
        is MainUiState.NavigateToPrayersSchedule -> {

            val startDestination = if (state is MainUiState.NavigateToLocationSearch)
                LOCATION_SEARCH
            else
                PRAYERS_SCHEDULE

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable(LOCATION_SEARCH) {
                    LocationSearchScreen(
                        onNavigateToSchedule = { _ ->
                            navController.navigate(PRAYERS_SCHEDULE) {
                                popUpTo(PRAYERS_SCHEDULE) { inclusive = true }
                            }
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(PRAYERS_SCHEDULE) {
                    PrayersScheduleScreen(
                        onNavigateToLocationSearch = {
                            navController.navigate(LOCATION_SEARCH) {
                                popUpTo(LOCATION_SEARCH)
                            }
                        }
                    )
                }
            }
        }

        else -> {}
    }


}