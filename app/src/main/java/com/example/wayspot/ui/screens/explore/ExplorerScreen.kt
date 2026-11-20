package com.example.wayspot.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.savedplace.SavedPlace
import com.example.wayspot.data.model.savedplace.SavedPlacesRules
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.explore.components.ExplorerPopularCard
import com.example.wayspot.ui.screens.explore.components.ExplorerTags
import com.example.wayspot.ui.theme.WayspotTheme

/** Adaptador entre el estado de exploración, los guardados compartidos y el contenido sin estado. */
@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    onPlaceClick: (String) -> Unit,
    savedPlaces: List<SavedPlace>,
    onSaveClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by exploreViewModel.uiState.collectAsState()

    LaunchedEffect(savedPlaces) {
        exploreViewModel.updateSavedPlaces(savedPlaces)
    }

    ExplorerContent(
        places = state.places,
        searchText = state.searchText,
        categories = state.categories,
        isLoading = state.isLoading,
        loadFailed = state.loadFailed,
        selectedCategory = state.selectedCategory,
        onSearchChange = {
            exploreViewModel.updateSearchText(it)
        },
        onCategorySelect = {
            exploreViewModel.updateSelectedCategory(it)
        },
        onPlaceClick = onPlaceClick,
        savedPlaceIds = state.savedPlaceIds,
        onSaveClick = onSaveClick,
        modifier = modifier
    )
}

/** Muestra búsqueda, filtros y el catálogo en una cuadrícula construida por filas estables. */
@Composable
fun ExplorerContent(
    places: List<Place>,
    searchText: String,
    categories: List<String>,
    selectedCategory: String?,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    onPlaceClick: (String) -> Unit,
    savedPlaceIds: Set<String>,
    onSaveClick: (Place) -> Unit,
    isLoading: Boolean = false,
    loadFailed: Boolean = false,
    modifier: Modifier = Modifier
) {
    val hasFilters = searchText.isNotBlank() || selectedCategory != null
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            bottom = 16.dp
        )
    ) {

        item {
            WayspotSearch(
                searchText = searchText,
                onSearchChange = onSearchChange,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        item {
            ExplorerTags(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategoryClick = onCategorySelect,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (hasFilters) {
                        stringResource(R.string.explore_results_count, places.size)
                    } else {
                        stringResource(R.string.popular_destinations_title)
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (hasFilters) {
                    Text(
                        text = stringResource(R.string.see_all),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onSearchChange("")
                            onCategorySelect(null)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) { CircularProgressIndicator() }
            }
        } else if (loadFailed || places.isEmpty()) {
            item {
                Text(
                    text = stringResource(
                        if (loadFailed) R.string.home_load_places_error
                        else if (hasFilters) R.string.explore_no_results
                        else R.string.home_places_no_data
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }

        val rows = places.chunked(2)

        items(
            count = rows.size,
            key = { rowIndex ->
                rows[rowIndex].joinToString(
                    separator = "|"
                ) {
                    it.id
                }
            }
        ) { rowIndex ->

            val rowItems = rows[rowIndex]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                rowItems.forEach { place ->

                    ExplorerPopularCard(
                        place = place,
                        isSaved = place.id in savedPlaceIds,

                        onSaveClick = {
                            onSaveClick(place)
                        },

                        onClick = {
                            onPlaceClick(place.id)
                        },

                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ExploreScreenPreview() {
    WayspotTheme {
        ExplorerContent(
            places = PreviewDataPopular.listPlaces,
            searchText = "",
            categories = listOf("Playa", "Montaña", "Museo"),
            selectedCategory = null,
            onSearchChange = {},
            onCategorySelect = {},
            onPlaceClick = {},
            savedPlaceIds = SavedPlacesRules.savedPlaceIds(
                PreviewData.savedPlaces
            ),
            onSaveClick = {}
        )
    }
}
