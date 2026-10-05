package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.data.model.HomeRules
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.home.components.HomeCategoryChips
import com.example.wayspot.ui.screens.home.components.HomeFeaturedPlanCarousel
import com.example.wayspot.ui.screens.home.components.HomeIntroSection
import com.example.wayspot.ui.screens.home.components.HomeReviewCard
import com.example.wayspot.ui.screens.home.components.HomeReviewsEmptyState
import com.example.wayspot.ui.screens.home.components.HomeReviewsHeader
import com.example.wayspot.ui.theme.WayspotTheme

/** Adaptador de inicio entre [HomeViewModel], lugares guardados compartidos y contenido sin estado. */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    savedPlaces: List<SavedPlace>,
    onPlaceClick: (String) -> Unit,
    onSaveClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by homeViewModel.uiState.collectAsState()

    LaunchedEffect(savedPlaces) {
        homeViewModel.updateSavedPlaces(savedPlaces)
    }

    HomeContent(
        state = state,
        onSearchQueryChange = homeViewModel::updateSearchQuery,
        onCategoryClick = homeViewModel::selectCategory,
        onPreviousFeaturedPlanClick = homeViewModel::showPreviousFeaturedPlan,
        onNextFeaturedPlanClick = homeViewModel::showNextFeaturedPlan,
        onFeaturedLikeClick = homeViewModel::toggleFeaturedLike,
        onReviewLikeClick = homeViewModel::toggleReviewLike,
        onReviewExpandClick = homeViewModel::toggleReviewExpanded,
        onReviewCommentClick = homeViewModel::registerReviewComment,
        onReviewShareClick = homeViewModel::toggleReviewShared,
        onSaveClick = onSaveClick,
        onPlaceClick = onPlaceClick,
        modifier = modifier
    )
}

/** Renderiza las secciones de inicio a partir del estado recibido y propaga todas las acciones. */
@Composable
fun HomeContent(
    state: HomeState,
    onSearchQueryChange: (String) -> Unit,
    onCategoryClick: (HomeCategoryId) -> Unit,
    onPreviousFeaturedPlanClick: () -> Unit,
    onNextFeaturedPlanClick: () -> Unit,
    onFeaturedLikeClick: (String) -> Unit,
    onReviewLikeClick: (String) -> Unit,
    onReviewExpandClick: (String) -> Unit,
    onReviewCommentClick: (String) -> Unit,
    onReviewShareClick: (String) -> Unit,
    onSaveClick: (Place) -> Unit,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val placesById = state.places.associateBy { place -> place.id }
    val visibleReviews = state.reviews.filter { review ->
        val place = placesById[review.placeId] ?: return@filter false
        HomeRules.matchesSearch(
            query = state.searchQuery,
            candidates = listOf(
                context.getString(review.authorNameRes),
                context.getString(review.authorHandleRes),
                context.getString(review.bodyRes),
                place.title,
                place.location
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)
    ) {
        item(key = HomeSectionKey.INTRO) {
            HomeIntroSection(
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item(key = HomeSectionKey.CATEGORIES) {
            HomeCategoryChips(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onCategoryClick = onCategoryClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item(key = HomeSectionKey.FEATURED) {
            HomeFeaturedPlanCarousel(
                places = state.places,
                activePlanIndex = state.activeFeaturedPlanIndex,
                likeCounts = state.featuredLikeCounts,
                likedPlanIds = state.likedFeaturedPlanIds,
                savedPlaceIds = state.savedPlaceIds,
                onPreviousClick = onPreviousFeaturedPlanClick,
                onNextClick = onNextFeaturedPlanClick,
                onLikeClick = onFeaturedLikeClick,
                //onSaveClick = onSaveClick,
                onSaveClick = {},
                onPlaceClick = onPlaceClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item(key = HomeSectionKey.REVIEWS_HEADER) {
            HomeReviewsHeader(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
        if (visibleReviews.isEmpty()) {
            item(key = HomeSectionKey.EMPTY_REVIEWS) {
                HomeReviewsEmptyState(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }
        } else {
            items(
                items = visibleReviews,
                key = { review -> review.id }
            ) { review ->
                placesById[review.placeId]?.let { place ->
                    HomeReviewCard(
                        review = review,
                        place = place,
                        likeCount = state.reviewLikeCounts[review.id] ?: review.initialLikeCount,
                        commentCount = state.reviewCommentCounts[review.id] ?: review.initialCommentCount,
                        isLiked = review.id in state.likedReviewIds,
                        isExpanded = review.id in state.expandedReviewIds,
                        isShared = review.id in state.sharedReviewIds,
                        isSaved = place.id in state.savedPlaceIds,
                        onPlaceClick = { onPlaceClick(place.id) },
                        onLikeClick = { onReviewLikeClick(review.id) },
                        onExpandClick = { onReviewExpandClick(review.id) },
                        onCommentClick = { onReviewCommentClick(review.id) },
                        onShareClick = { onReviewShareClick(review.id) },
                        //onSaveClick = { onSaveClick(place) },
                        onSaveClick = {},
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

private enum class HomeSectionKey {
    INTRO,
    CATEGORIES,
    FEATURED,
    REVIEWS_HEADER,
    EMPTY_REVIEWS
}

@WayspotMultiPreview
@Composable
private fun HomeScreenPreview() {
    val selectedCategory = HomeCategoryId.NATURE
    WayspotTheme {
        HomeContent(
            state = HomeState(
                selectedCategory = selectedCategory,
                categories = PreviewData.homeCategories,
                featuredPlans = HomeRules.filterFeaturedPlans(
                    plans = PreviewData.homeFeaturedPlans,
                    categoryId = selectedCategory
                ),
                reviews = PreviewData.homeReviews,
                places = emptyList(),
                featuredLikeCounts = PreviewData.homeFeaturedPlans.associate { plan ->
                    plan.id to plan.initialLikeCount
                },
                reviewLikeCounts = PreviewData.homeReviews.associate { review ->
                    review.id to review.initialLikeCount
                },
                reviewCommentCounts = PreviewData.homeReviews.associate { review ->
                    review.id to review.initialCommentCount
                }
            ),
            onSearchQueryChange = {},
            onCategoryClick = {},
            onPreviousFeaturedPlanClick = {},
            onNextFeaturedPlanClick = {},
            onFeaturedLikeClick = {},
            onReviewLikeClick = {},
            onReviewExpandClick = {},
            onReviewCommentClick = {},
            onReviewShareClick = {},
            onSaveClick = {},
            onPlaceClick = {}
        )
    }
}
