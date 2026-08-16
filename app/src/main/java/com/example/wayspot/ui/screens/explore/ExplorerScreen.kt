package com.example.wayspot.ui.screens.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.model.Places
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.PreviewDataPopular
import com.example.wayspot.ui.screens.explore.components.ExplorerPopularCard
import com.example.wayspot.ui.screens.explore.components.ExplorerTags
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Playas") }
    val places = PreviewDataPopular.listPlaces

    ExplorerContent(
        places = places,
        searchText = searchText,
        selectedCategory = selectedCategory,
        onSearchChange = { searchText = it },
        onCategorySelect = { selectedCategory = it },
        modifier = modifier
    )
}

@Composable
fun ExplorerContent(
    places: List<Places>,
    searchText: String,
    selectedCategory: String,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
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
                selectedCategory = selectedCategory,
                onCategoryClick = onCategorySelect,
                modifier = Modifier.fillMaxWidth()
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
                    text = "Destinos populares",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver todos",
                    fontSize = 14.sp,
                    color = Color(0xFF3F7EE8),
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
                        modifier = Modifier.weight(1f)
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

@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    WayspotTheme {
        ExploreScreen()
    }
}
