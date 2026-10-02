package io.athan.feature.prayersschedule.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.athan.core.designsystem.component.card.CurrentPrayerCard
import io.athan.core.designsystem.component.feedback.ErrorMessage
import io.athan.core.designsystem.component.feedback.IconMessage
import io.athan.core.designsystem.component.card.PrayerItemRow
import io.athan.core.designsystem.theme.AthanSpacing
import io.athan.feature.location.presentation.model.LocationUiModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayersScheduleScreen(
    onNavigateToLocationSearch: () -> Unit,
    viewModel: PrayersScheduleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                PrayersScheduleSideEffect.OnNavigateToLocation -> onNavigateToLocationSearch()
            }
        }
    }

    PrayersScheduleContent(
        uiState = uiState,
        onIntent = viewModel::processIntent
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayersScheduleContent(
    uiState: PrayersScheduleUiState, onIntent: (PrayersScheduleIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Athan") })
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(AthanSpacing.small)
                    .align(Alignment.TopCenter),
            ) {
                PrayersScheduleHeader(
                    selectedDate = uiState.formattedDate,
                    selectedLocation = uiState.selectedLocation,
                    onDateClick = { onIntent(PrayersScheduleIntent.OnDatePickerClicked) },
                    onLocationClick = { onIntent(PrayersScheduleIntent.OnCurrentLocationClicked) })

                Spacer(modifier = Modifier.height(AthanSpacing.small))

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(
                                    AthanSpacing.small
                                )
                            ) {
                                ErrorMessage(message = uiState.error)
                                FilledTonalButton(
                                    onClick = { onIntent(PrayersScheduleIntent.OnRetryClicked) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null // No need to be read by TalkBack
                                    )
                                    Text("Refresh")
                                }
                            }
                        }
                    }

                    else -> {
                        if (uiState.upcomingPrayersTimes.isNotEmpty() || uiState.pastPrayersTimes.isNotEmpty()) {
                            PrayersTimesListContent(
                                uiState = uiState,
                                onTogglePastPrayers = { onIntent(PrayersScheduleIntent.OnPastPrayersClicked) },
                                onIntent = onIntent
                            )
                        } else {
                            IconMessage(
                                message = "No prayers to show",
                                icon = Icons.Default.HourglassEmpty
                            )
                        }
                    }
                }
            }

            if (uiState.showDatePicker) {
                DatePickerDialog(
                    initialSelectedDateMillis = uiState.selectedDateMillis,
                    onDateSelected = {
                        onIntent(
                            PrayersScheduleIntent.OnDateSelected(
                                it
                            )
                        )
                    },
                    onDismiss = {
                        onIntent(
                            PrayersScheduleIntent.OnDatePickerDismiss
                        )
                    })
            }
        }
    }
}

@Composable
fun PrayersTimesListContent(
    uiState: PrayersScheduleUiState,
    onTogglePastPrayers: () -> Unit,
    onIntent: (PrayersScheduleIntent) -> Unit
) {

    val pullToRefreshState = rememberPullToRefreshState()

    val onlyPastPrayers =
        uiState.pastPrayersTimes.isNotEmpty() && uiState.upcomingPrayersTimes.isEmpty()

    uiState.currentPrayerTime?.let {
        CurrentPrayerCard(
            name = it.name, time = it.time, isNow = true
        )
    }

    Spacer(modifier = Modifier.padding(AthanSpacing.small))

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { onIntent(PrayersScheduleIntent.OnPullToRefresh) }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(AthanSpacing.small)
        ) {
            if (onlyPastPrayers) {
                uiState.pastPrayersTimes.forEach { prayer ->
                    item {
                        PrayerItemRow(
                            name = prayer.name,
                            time = prayer.time,
                            isPast = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                if (uiState.pastPrayersTimes.isNotEmpty()) {
                    item(key = "toggle_past_prayers") {
                        TextButton(
                            onClick = { onTogglePastPrayers() },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (uiState.showPastPrayers) {
                                Text("Hide past prayers")
                            } else {
                                Text("Show past prayers")
                            }
                        }
                    }

                    item(key = "past_prayers_accordion") {
                        AnimatedVisibility(
                            visible = uiState.showPastPrayers,
                            enter = expandVertically(animationSpec = tween(300)) + fadeIn(
                                animationSpec = tween(300)
                            ),
                            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(
                                animationSpec = tween(300)
                            ),
                            modifier = Modifier.animateItem()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AthanSpacing.small)
                            ) {
                                uiState.pastPrayersTimes.forEach { prayer ->
                                    PrayerItemRow(
                                        name = prayer.name,
                                        time = prayer.time,
                                        isPast = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(
                items = uiState.upcomingPrayersTimes,
                key = { "upcoming_${it.name}" }) { prayerTime ->


                PrayerItemRow(
                    name = prayerTime.name,
                    time = prayerTime.time,
                    isPast = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PrayersScheduleHeader(
    selectedDate: String,
    selectedLocation: LocationUiModel?,
    onDateClick: () -> Unit,
    onLocationClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = { onDateClick() },
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null, // No need to be read by TalkBack
                modifier = Modifier.padding(end = AthanSpacing.small)
            )
            Text(selectedDate)
        }

        TextButton(onClick = { onLocationClick() }) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null, // No need to be read by TalkBack
                modifier = Modifier.padding(end = AthanSpacing.small)
            )
            Text(
                "${selectedLocation?.name}, ${selectedLocation?.country}"
            )
        }
    }
}
