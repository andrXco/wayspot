package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wayspot.ui.components.WaySpotSearchBar
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeTopSection(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HomeHeader(onNotificationsClick = onNotificationsClick)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        WaySpotSearchBar(
            value = searchText,
            onValueChange = onSearchChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeTopSectionPreview() {
    WayspotTheme {
        HomeTopSection(
            searchText = "",
            onSearchChange = {},
            onNotificationsClick = {}
        )
    }
}
