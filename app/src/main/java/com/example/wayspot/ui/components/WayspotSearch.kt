package com.example.wayspot.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun WayspotSearch(
    searchText: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    WaySpotSearchBar(
        value = searchText,
        onValueChange = onSearchChange,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun WayspotSearchPreview() {
    WayspotTheme {
        WayspotSearch(
            searchText = "",
            onSearchChange = {}
        )
    }
}
