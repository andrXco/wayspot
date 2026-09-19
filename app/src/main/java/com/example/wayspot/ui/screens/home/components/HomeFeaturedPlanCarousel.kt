package com.example.wayspot.ui.screens.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wayspot.R
import com.example.wayspot.data.model.HomeFeaturedPlan
import com.example.wayspot.data.model.Place

/**
 * Presenta el plan destacado activo y delega al padre el desplazamiento, los favoritos y el
 * guardado del lugar asociado.
 */
@Composable
fun HomeFeaturedPlanCarousel(
    plans: List<HomeFeaturedPlan>,
    places: List<Place>,
    activePlanIndex: Int,
    likeCounts: Map<String, Int>,
    likedPlanIds: Set<String>,
    savedPlaceIds: Set<String>,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onLikeClick: (String) -> Unit,
    onSaveClick: (Place) -> Unit,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val plan = plans.getOrNull(activePlanIndex) ?: return
    val place = places.firstOrNull { candidate -> candidate.id == plan.placeId } ?: return
    val placeTitle = stringResource(place.tituloRes)
    val isLiked = plan.id in likedPlanIds
    val isSaved = place.id in savedPlaceIds
    val imageContentColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val ratingColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(208.dp)
                    .clickable { onPlaceClick(place.id) }
            ) {
                AsyncImage(
                    model = place.imagen ?: R.drawable.post_card_machu_pichu,
                    contentDescription = stringResource(
                        R.string.home_featured_image_content_description,
                        placeTitle
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.branding_logo_claro_wayspot),
                    error = painterResource(R.drawable.branding_logo_claro_wayspot)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.02f),
                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.82f)
                                )
                            )
                        )
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = stringResource(plan.badgeRes),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                if (plans.size > 1) {
                    CarouselControl(
                        image = Icons.Rounded.ChevronLeft,
                        contentDescriptionRes = R.string.home_featured_previous_content_description,
                        contentColor = imageContentColor,
                        onClick = onPreviousClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    CarouselControl(
                        image = Icons.Rounded.ChevronRight,
                        contentDescriptionRes = R.string.home_featured_next_content_description,
                        contentColor = imageContentColor,
                        onClick = onNextClick,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = placeTitle,
                            color = imageContentColor,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(place.ubicacionRes),
                            color = imageContentColor.copy(alpha = 0.86f),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        modifier = Modifier.clickable { onPlaceClick(place.id) },
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.home_featured_cta),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeFeaturedAction(
                        image = if (isLiked) Icons.Filled.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = stringResource(
                            if (isLiked) {
                                R.string.home_featured_unlike_content_description
                            } else {
                                R.string.home_featured_like_content_description
                            },
                            placeTitle
                        ),
                        tint = if (isLiked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { onLikeClick(plan.id) }
                    )
                    Text(
                        text = stringResource(
                            R.string.home_review_action_count,
                            likeCounts[plan.id] ?: plan.initialLikeCount
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = ratingColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = place.rating.toString(),
                        color = ratingColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    HomeFeaturedAction(
                        image = if (isSaved) Icons.Filled.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = stringResource(
                            if (isSaved) {
                                R.string.home_featured_unsave_content_description
                            } else {
                                R.string.home_featured_save_content_description
                            },
                            placeTitle
                        ),
                        tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { onSaveClick(place) }
                    )
                }
                HomeCarouselDots(
                    count = plans.size,
                    activeIndex = activePlanIndex,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CarouselControl(
    image: ImageVector,
    @StringRes contentDescriptionRes: Int,
    contentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(6.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.42f),
        contentColor = contentColor
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = image,
                contentDescription = stringResource(contentDescriptionRes)
            )
        }
    }
}

@Composable
private fun HomeFeaturedAction(
    image: ImageVector,
    contentDescription: String,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(36.dp)
    ) {
        Icon(
            imageVector = image,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun HomeCarouselDots(
    count: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .width(if (index == activeIndex) 20.dp else 6.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == activeIndex) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                        }
                    )
            )
        }
    }
}
