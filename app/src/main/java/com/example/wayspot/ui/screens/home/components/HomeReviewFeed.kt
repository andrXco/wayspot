package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.R

@Composable
fun HomeReviewsHeader(
    isFollowingOnly: Boolean,
    isLoadingFollowingUsers: Boolean,
    onFollowingFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(
                text = stringResource(R.string.home_reviews_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = stringResource(R.string.home_reviews_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
        FilterChip(
            selected = isFollowingOnly,
            onClick = onFollowingFilterClick,
            enabled = !isLoadingFollowingUsers,
            label = {
                Text(
                    text = stringResource(
                        when {
                            isLoadingFollowingUsers -> R.string.home_reviews_filter_loading
                            isFollowingOnly -> R.string.home_reviews_filter_active
                            else -> R.string.home_reviews_filter_following
                        }
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            modifier = Modifier.height(36.dp)
        )
    }
}

@Composable
fun HomeReviewsEmptyState(
    isFollowingOnly: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(
            if (isFollowingOnly) R.string.home_reviews_empty_following
            else R.string.home_reviews_empty
        ),
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun HomeReviewsLoadingState(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        Text(
            text = stringResource(R.string.home_reviews_following_loading),
            modifier = Modifier.padding(start = 10.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun HomeReviewsFilterError(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.home_reviews_following_error),
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}
