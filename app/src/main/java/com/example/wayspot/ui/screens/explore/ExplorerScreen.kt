package com.example.wayspot.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.ExploreCategory
import com.example.wayspot.data.model.ExploreCategoryRules
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.explore.components.ExplorerPopularCard
import com.example.wayspot.ui.screens.explore.components.ExplorerTags
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ExploreScreen(
    onPlaceClick: (String) -> Unit,
    savedPlaces: List<SavedPlace>,
    onSaveClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf(ExploreCategoryRules.initialCategory)
    }

    val places = PreviewDataPopular.listPlaces
    val filteredPlaces = ExploreCategoryRules.filterPlaces(
        places = places,
        selectedCategory = selectedCategory
    )

    ExplorerContent(
        places = filteredPlaces,
        searchText = searchText,
        categories = ExploreCategoryRules.categories,
        selectedCategory = selectedCategory,
        onSearchChange = {
            searchText = it
        },
        onCategorySelect = {
            selectedCategory = it
        },
        onPlaceClick = onPlaceClick,
        savedPlaceIds = savedPlaces.mapTo(mutableSetOf()) {
            it.place.id
        },
        onSaveClick = onSaveClick,
        modifier = modifier
    )
}

@Composable
fun ExplorerContent(
    places: List<Place>,
    searchText: String,
    categories: List<ExploreCategory>,
    selectedCategory: ExploreCategory,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (ExploreCategory) -> Unit,
    onPlaceClick: (String) -> Unit,
    savedPlaceIds: Set<String>,
    onSaveClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = stringResource(
                        R.string.popular_destinations_title
                    ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(
                        R.string.see_all
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
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
        ExploreScreen(
            onPlaceClick = {},
            savedPlaces = PreviewData.savedPlaces,
            onSaveClick = {}
        )
    }
}
