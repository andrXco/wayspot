package com.example.wayspot.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.data.model.Places
import com.example.wayspot.R
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.explore.components.ExplorerPopularCard
import com.example.wayspot.ui.screens.explore.components.ExplorerTags
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ExploreScreen(
    onPlaceClick: (Places) -> Unit,
    m: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategoryRes by remember { mutableIntStateOf(R.string.category_beaches) }
    val places = PreviewDataPopular.listPlaces

    ExplorerContent(
        places = places,
        searchText = searchText,
        selectedCategoryRes = selectedCategoryRes,
        onSearchChange = { searchText = it },
        onCategorySelect = { selectedCategoryRes = it },
        onPlaceClick = onPlaceClick,
        m = m
    )
}

@Composable
fun ExplorerContent(
    places: List<Places>,
    searchText: String,
    selectedCategoryRes: Int,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (Int) -> Unit,
    onPlaceClick: (Places) -> Unit,
    m: Modifier = Modifier
) {
    LazyColumn(
        modifier = m
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Buscador
        item {
            WayspotSearch(
                searchText = searchText,
                onSearchChange = onSearchChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Filtros de Categorías
        item {
            ExplorerTags(
                selectedCategoryRes = selectedCategoryRes,
                onCategoryClick = onCategorySelect,
                m = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Título de Sección
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.popular_destinations_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.see_all),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* Acción Ver todos */ }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Cuadrícula de Destinos (agrupados de 2 en 2)
        val rows = places.chunked(2)
        items(rows.size) { rowIndex ->
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
                        onSaveClick = { /* Acción Guardar */ },
                        onClick = { onPlaceClick(place) },
                        m = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ExploreScreenPreview() {
    WayspotTheme {
        ExploreScreen(onPlaceClick = {})
    }
}
