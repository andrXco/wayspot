package com.example.wayspot.ui.screens.savedplaces

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.ui.components.WaySpotSearchBar
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.savedplaces.components.SavedPlaceCard
import com.example.wayspot.ui.screens.savedplaces.components.SavedPlacesCategoryTabs
import com.example.wayspot.ui.screens.savedplaces.components.SavedPlacesEmptyState
import com.example.wayspot.ui.screens.savedplaces.components.SavedPlacesHeader
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SavedPlacesScreen(
    savedPlacesViewModel: SavedPlacesViewModel,
    savedPlaces: List<SavedPlace>,
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onRemoveFromList: (String, SavedPlaceList) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by savedPlacesViewModel.uiState.collectAsState()

    val currentLocale = LocalConfiguration.current.locales[0]

    val localizedSearchTerms = savedPlaces.associate { savedPlace ->
        savedPlace.place.id to listOf(
            stringResource(savedPlace.place.tituloRes),
            stringResource(savedPlace.place.ubicacionRes),
            stringResource(savedPlace.place.categoriaRes)
        )
    }

    val counts = SavedPlaceList.entries.associateWith { list ->
        savedPlaces.count { savedPlace ->
            list in savedPlace.lists
        }
    }

    val normalizedQuery =
        state.searchQuery.trim().lowercase(currentLocale)

    val filteredSavedPlaces = savedPlaces.filter { savedPlace ->
        state.selectedList in savedPlace.lists &&
                (
                        normalizedQuery.isEmpty() ||
                                localizedSearchTerms
                                    .getValue(savedPlace.place.id)
                                    .any { value ->
                                        value
                                            .lowercase(currentLocale)
                                            .contains(normalizedQuery)
                                    }
                        )
    }

    SavedPlacesContent(
        destinationCount = savedPlaces.size,
        savedPlaces = filteredSavedPlaces,
        selectedList = state.selectedList,
        counts = counts,
        searchQuery = state.searchQuery,
        onSearchQueryChange = {
            savedPlacesViewModel.updateSearchQuery(it)
        },
        onListSelected = {
            savedPlacesViewModel.updateSelectedList(it)
        },
        onBackClick = onBackClick,
        onPlaceClick = onPlaceClick,
        onRemoveClick = { placeId ->
            onRemoveFromList(
                placeId,
                state.selectedList
            )
        },
        modifier = modifier
    )
}

@Composable
fun SavedPlacesContent(
    destinationCount: Int,
    savedPlaces: List<SavedPlace>,
    selectedList: SavedPlaceList,
    counts: Map<SavedPlaceList, Int>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onListSelected: (SavedPlaceList) -> Unit,
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            bottom = 24.dp
        )
    ) {
        item {
            SavedPlacesHeader(
                destinationCount = destinationCount,
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            WaySpotSearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp,
                        vertical = 8.dp
                    ),
                placeholder = stringResource(
                    R.string.saved_places_search_placeholder
                )
            )
        }

        item {
            SavedPlacesCategoryTabs(
                selectedList = selectedList,
                counts = counts,
                onListSelected = onListSelected,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (savedPlaces.isEmpty()) {
            item {
                SavedPlacesEmptyState(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            items(
                items = savedPlaces,
                key = { savedPlace ->
                    savedPlace.place.id
                }
            ) { savedPlace ->

                SavedPlaceCard(
                    savedPlace = savedPlace,

                    onDetailsClick = {
                        onPlaceClick(
                            savedPlace.place.id
                        )
                    },

                    onRemoveClick = {
                        onRemoveClick(
                            savedPlace.place.id
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 6.dp
                        )
                )
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun SavedPlacesScreenPreview() {
    val savedPlaces = PreviewData.savedPlaces
    val selectedList = SavedPlacesRules.defaultList

    WayspotTheme {
        SavedPlacesContent(
            destinationCount = savedPlaces.size,
            savedPlaces = savedPlaces.filter { savedPlace ->
                selectedList in savedPlace.lists
            },
            selectedList = selectedList,
            counts = SavedPlaceList.entries.associateWith { list ->
                savedPlaces.count { savedPlace ->
                    list in savedPlace.lists
                }
            },
            searchQuery = "",
            onSearchQueryChange = {},
            onListSelected = {},
            onBackClick = {},
            onPlaceClick = {},
            onRemoveClick = {}
        )
    }
}