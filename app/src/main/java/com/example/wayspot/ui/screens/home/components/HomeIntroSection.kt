package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeIntroSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        WayspotSearch(
            searchText = searchQuery,
            onSearchChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.home_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.home_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun HomeIntroSectionPreview() {
    WayspotTheme {
        Surface {
            HomeIntroSection(
                searchQuery = "",
                onSearchQueryChange = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
