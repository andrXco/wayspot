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
import com.example.wayspot.data.local.PreviewData
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
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

/** Adaptador de inicio entre [HomeViewModel], lugares guardados compartidos y contenido sin estado. */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    savedPlaces: List<SavedPlace>,
    onPlaceClick: (String) -> Unit,
    onReviewClick: (String) -> Unit,
    onReviewCommentClick: (String) -> Unit,
    onAuthorClick: (String) -> Unit,
    onSaveClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by homeViewModel.uiState.collectAsState()

    LaunchedEffect(savedPlaces) {
        homeViewModel.updateSavedPlaces(savedPlaces)
    }

    LaunchedEffect(Unit) {
        homeViewModel.refresh()
    }

    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.errorMessage ?: ""
                )
            }
        }

        else -> {
            HomeContent(
                state = state,
                onSearchQueryChange = homeViewModel::updateSearchQuery,
                onCategoryClick = homeViewModel::selectCategory,
                onPreviousFeaturedPlanClick = homeViewModel::showPreviousFeaturedPlan,
                onNextFeaturedPlanClick = homeViewModel::showNextFeaturedPlan,
                onFeaturedLikeClick = homeViewModel::toggleFeaturedLike,
                onReviewLikeClick = homeViewModel::toggleReviewLike,
                onReviewExpandClick = homeViewModel::toggleReviewExpanded,
                onReviewCommentClick = onReviewCommentClick,
                onReviewShareClick = homeViewModel::toggleReviewShared,
                onSaveClick = onSaveClick,
                onPlaceClick = onPlaceClick,
                onReviewClick = onReviewClick,
                onAuthorClick = onAuthorClick,
                modifier = modifier
            )
        }
    }
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
    onReviewClick: (String) -> Unit,
    onAuthorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val visibleReviews = state.reviews.filter { review ->
        HomeRules.matchesSearch(
            query = state.searchQuery,
            candidates = listOf(
                review.author.name,
                review.author.username,
                review.review.title,
                review.review.description,
                review.place.title,
                review.place.location
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
                key = { review -> review.review.id }
            ) { review ->
                HomeReviewCard(
                    item = review,
                    likeCount = state.reviewLikeCounts[review.review.id] ?: 0,
                    isLiked = review.review.id in state.likedReviewIds,
                    isExpanded = review.review.id in state.expandedReviewIds,
                    isShared = review.review.id in state.sharedReviewIds,
                    onReviewClick = { onReviewClick(review.review.id) },
                    onAuthorClick = { onAuthorClick(review.author.id.toString()) },
                    onPlaceClick = { onPlaceClick(review.place.id) },
                    onLikeClick = { onReviewLikeClick(review.review.id) },
                    onExpandClick = { onReviewExpandClick(review.review.id) },
                    onCommentClick = { onReviewCommentClick(review.review.id) },
                    onShareClick = { onReviewShareClick(review.review.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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
                reviews = emptyList(),
                places = emptyList(),
                featuredLikeCounts = PreviewData.homeFeaturedPlans.associate { plan ->
                    plan.id to plan.initialLikeCount
                },
                reviewLikeCounts = emptyMap()
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
            onPlaceClick = {},
            onReviewClick = {},
            onAuthorClick = {}
        )
    }
}
