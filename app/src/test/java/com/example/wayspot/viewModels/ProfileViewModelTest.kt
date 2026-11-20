package com.example.wayspot.viewModels

import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.model.achievement.AchievementType
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.data.repository.UserRepository
import com.example.wayspot.ui.screens.profile.ProfileViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val userRepository: UserRepository = mockk()
    private val reviewRepository: ReviewRepository = mockk()
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val authRepository: AuthRepository = mockk()
        val user: FirebaseUser = mockk()
        every { user.uid } returns "usuario"
        every { authRepository.currentUser } returns user
        val placeRepository: PlaceRepository = mockk()
        coEvery { placeRepository.getPlaces() } returns Result.success(emptyList())
        viewModel = ProfileViewModel(authRepository, userRepository, placeRepository, reviewRepository)
    }

    // Las fotos del DTO cuentan aunque la consulta de likes personales falle.
    @Test
    fun loadReviews_whenLikedLookupFails_keepsAchievementsFromReviews() = runTest(testDispatcher) {
        val reviews = (1..3).map { index ->
            ReviewFirestoreDto(
                id = "review-$index",
                userId = "usuario",
                photoUrls = listOf("https://storage/photo-$index"),
                likesCount = 4
            ).toReviewInfo()
        }
        coEvery { userRepository.getReviewsByUserId("usuario") } returns Result.success(reviews)
        coEvery { reviewRepository.getLikedReviewIds(reviews.map { it.id }) } returns
            Result.failure(IllegalStateException("Falló la consulta de likes"))

        viewModel.loadReviews()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.reviews).isEqualTo(reviews)
        assertThat(state.areReviewLikesLoaded).isFalse()
        assertThat(state.achievements.filter { it.isUnlocked }.map { it.type })
            .containsExactly(AchievementType.FIRST_REVIEW, AchievementType.PHOTOGRAPHER, AchievementType.INFLUENCER)
    }

    // El contador y la insignia cruzan juntos el umbral al dar y quitar un like.
    @Test
    fun toggleReviewLike_updatesReviewCountAndInfluencerProgress() = runTest(testDispatcher) {
        val review = ReviewFirestoreDto(id = "review-1", userId = "usuario", likesCount = 9)
            .toReviewInfo()
        coEvery { userRepository.getReviewsByUserId("usuario") } returns Result.success(listOf(review))
        coEvery { reviewRepository.getLikedReviewIds(listOf(review.id)) } returns Result.success(emptySet())
        coEvery { reviewRepository.sendOrDeleteReviewLike(review.id) } returns Result.success(Unit)
        viewModel.loadReviews()
        advanceUntilIdle()

        viewModel.toggleReviewLike(review.id)
        advanceUntilIdle()

        val likedState = viewModel.uiState.value
        assertThat(likedState.reviewLikeCounts[review.id]).isEqualTo(10)
        assertThat(likedState.likedReviewIds).contains(review.id)
        assertThat(likedState.achievements.single { it.type == AchievementType.INFLUENCER }.isUnlocked).isTrue()

        viewModel.toggleReviewLike(review.id)
        advanceUntilIdle()

        val unlikedState = viewModel.uiState.value
        assertThat(unlikedState.reviewLikeCounts[review.id]).isEqualTo(9)
        assertThat(unlikedState.likedReviewIds).isEmpty()
        assertThat(unlikedState.achievements.single { it.type == AchievementType.INFLUENCER }.isUnlocked).isFalse()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
