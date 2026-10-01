package io.athan.feature.location.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.athan.core.designsystem.component.feedback.ErrorMessage
import io.athan.core.designsystem.component.feedback.IconMessage
import io.athan.core.designsystem.component.input.LocationSuggestionItem
import io.athan.core.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    onNavigateToSchedule: (Location) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: LocationSearchViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchBarState = rememberSearchBarState(initialValue = SearchBarValue.Collapsed)
    var isExpanded = searchBarState.currentValue == SearchBarValue.Expanded

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = uiState.query)) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is LocationSearchSideEffect.OnNavigateToSchedule -> {
                    onNavigateToSchedule(effect.selectedLocation)
                }

                is LocationSearchSideEffect.OnNavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(uiState.query) {
        if (uiState.query != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = uiState.query)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Athan") },
                navigationIcon = {
                    if (uiState.hasSavedLocation) {
                        IconButton(onClick = { viewModel.processIntent(LocationSearchIntent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp),
        ) {

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    viewModel.processIntent(LocationSearchIntent.OnQueryChanged(newValue.text))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Tap your city...") }
            )

//            SearchBar(
//                state = searchBarState,
//                inputField = {
//                    SearchBarDefaults.InputField(
//                        query = uiState.query,
//                        onQueryChange = {
//                            viewModel.processIntent(
//                                LocationSearchIntent.OnQueryChanged(
//                                    it
//                                )
//                            )
//                        },
//                        onSearch = { isExpanded = false },
//                        expanded = isExpanded,
//                        onExpandedChange = { isExpanded = it },
//                        placeholder = { Text("Tap your city") },
//                        leadingIcon = {
//                            Icon(Icons.Default.LocationOn, contentDescription = "")
//                        },
//                        trailingIcon = {
//                            if (uiState.query.isNotEmpty()) {
//                                IconButton(onClick = {
//                                    viewModel.processIntent(
//                                        LocationSearchIntent.OnQueryChanged(
//                                            ""
//                                        )
//                                    )
//                                }) {
//                                    Icon(Icons.Default.Clear, contentDescription = "Clear location")
//                                }
//                            }
//                        }
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//            )

            Spacer(modifier = Modifier.padding(5.dp))

            when (val state = uiState) {
                is LocationSearchUiState.Idle -> {}
                is LocationSearchUiState.Empty ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Spacer(modifier = Modifier.padding(10.dp))

                            IconMessage(
                                message = "No location found for \"${uiState.query}\"",
                                icon = Icons.Default.LocationOff
                            )
                        }
                    }

                is LocationSearchUiState.Error ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorMessage(message = state.message)
                    }

                is LocationSearchUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Spacer(modifier = Modifier.padding(10.dp))

                            CircularProgressIndicator()
                        }
                    }
                }

                is LocationSearchUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.predictions,
                            key = { prediction -> prediction.id },
                        ) {
                            LocationSuggestionItem(
                                name = it.name,
                                country = it.country,
                                onClick = {
                                    viewModel.processIntent(
                                        LocationSearchIntent.OnLocationSelected(
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}