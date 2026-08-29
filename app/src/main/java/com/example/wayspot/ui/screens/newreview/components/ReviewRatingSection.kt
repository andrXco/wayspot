package com.example.wayspot.ui.screens.newreview.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.ReviewRules
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ReviewRatingSection(
    rating: Int,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val normalizedRating = rating.coerceIn(0, ReviewRules.MAX_RATING)
    val activeStarColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    val currentRatingDescription = stringResource(
        R.string.new_review_current_rating_content_description,
        normalizedRating,
        ReviewRules.MAX_RATING
    )
    val ratingLabelRes = when (normalizedRating) {
        1 -> R.string.new_review_rating_1
        2 -> R.string.new_review_rating_2
        3 -> R.string.new_review_rating_3
        4 -> R.string.new_review_rating_4
        5 -> R.string.new_review_rating_5
        else -> null
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.new_review_experience_question),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .selectableGroup()
                .semantics {
                    stateDescription = currentRatingDescription
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(ReviewRules.MAX_RATING) { index ->
                val starRating = index + 1
                val selectRatingDescription = stringResource(
                    R.string.new_review_select_rating_content_description,
                    starRating
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .selectable(
                            selected = starRating == normalizedRating,
                            onClick = { onRatingSelected(starRating) },
                            role = Role.RadioButton
                        )
                        .semantics {
                            contentDescription = selectRatingDescription
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (starRating <= normalizedRating) {
                            activeStarColor
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        if (ratingLabelRes != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(ratingLabelRes),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ReviewRatingSectionPreview() {
    WayspotTheme {
        ReviewRatingSection(
            rating = ReviewRules.DEFAULT_RATING,
            onRatingSelected = {}
        )
    }
}
