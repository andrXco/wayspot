
package com.example.wayspot.ui.screens.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextButton
import com.example.wayspot.R
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewInfo

@Composable
fun MapReviewCard(
    place: PlaceInfo,
    review: ReviewInfo,
    onReviewClick: () -> Unit,
    modifier: Modifier = Modifier,
    reviewPosition: Int = 1,
    reviewCount: Int = 1,
    onPreviousReview: () -> Unit = {},
    onNextReview: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Text(
                text = place.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            review.title?.takeIf { it.isNotBlank() }?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = stringResource(
                        R.string.review_rating_value,
                        review.rating
                    ),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            review.authorName?.takeIf { it.isNotBlank() }?.let { name ->
                Text(
                    text = stringResource(
                        R.string.places_map_review_author,
                        name
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = review.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (reviewCount > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onPreviousReview) {
                        Text(
                            text = stringResource(
                                R.string.places_map_previous_review
                            )
                        )
                    }

                    Text(
                        text = stringResource(
                            R.string.places_map_review_position,
                            reviewPosition,
                            reviewCount
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )

                    TextButton(onClick = onNextReview) {
                        Text(
                            text = stringResource(
                                R.string.places_map_next_review
                            )
                        )
                    }
                }
            }

            Button(
                onClick = onReviewClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.places_map_open_review)
                )
            }
        }
    }
}
