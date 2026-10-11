package com.example.wayspot.data.local

import com.example.wayspot.R
import com.example.wayspot.data.model.HomeCategory
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.data.model.HomeFeaturedPlan
import com.example.wayspot.data.model.HomeReview
import com.example.wayspot.data.model.Notification
import com.example.wayspot.data.model.ProfileNotificationPreferences
import com.example.wayspot.data.model.Review
import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewInfo
import com.example.wayspot.data.model.ReviewRules
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.data.model.UserStats
import com.example.wayspot.data.model.UserInfo

/** Fuente centralizada de datos locales para previsualizaciones y flujos de interfaz no remotos. */
object PreviewData {
    const val reviewCommentPreviewReviewId = "preview-review"
    val reviewCommentDraft = "El recorrido fue muy agradable y las recomendaciones me sirvieron mucho."

    val reviewDetailAuthor = UserInfo(
        id = "2",
        name = "María Polo",
        username = "maria.polo",
        bio = null,
        location = null,
        avatarUrl = null
    )

    val reviewDetailReview = ReviewInfo(
        id = reviewCommentPreviewReviewId,
        rating = 5,
        title = "Visita recomendada",
        description = "El paisaje y los senderos son increíbles.",
        userId = reviewDetailAuthor.id,
        placeId = PreviewDataPopular.previewPlaceInfo.id
    )

    val reviewDetailComments = listOf(
        ReviewComment(
            id = "preview-comment-1",
            content = "El recorrido estaba muy tranquilo por la mañana.",
            reviewId = reviewDetailReview.id,
            author = UserInfo(
                id = "3",
                name = "Juan Pérez",
                username = "juan.perez",
                bio = null,
                location = null,
                avatarUrl = null
            )
        ),
        ReviewComment(
            id = "preview-comment-2",
            content = "Gracias por compartir la recomendación.",
            reviewId = reviewDetailReview.id,
            author = UserInfo(
                id = "4",
                name = "Camila Torres",
                username = "cami.torres",
                bio = null,
                location = null,
                avatarUrl = null
            )
        )
    )

    val savedPlaces = listOf(
        SavedPlace(
            place = PreviewDataPopular.playaBlanca,
            lists = setOf(
                SavedPlaceList.WANT_TO_VISIT,
                SavedPlaceList.FAVORITES
            )
        ),
        SavedPlace(
            place = PreviewDataPopular.parqueTayrona,
            lists = setOf(
                SavedPlaceList.WANT_TO_VISIT,
                SavedPlaceList.VISITED
            )
        ),
        SavedPlace(
            place = PreviewDataPopular.piedraDelPenol,
            lists = setOf(SavedPlaceList.FAVORITES)
        ),
        SavedPlace(
            place = PreviewDataPopular.canoCristales,
            lists = setOf(SavedPlaceList.VISITED)
        )
    )

    val newReviewDraft = ReviewDraft(
        placeId = PreviewDataPopular.samplePlaces1.id,
        rating = ReviewRules.DEFAULT_RATING,
        title = "",
        description = "",
        photoUris = emptyList()
    )

    val userProfile = UserProfile(
        name = "Valentina García",
        username = "valentina_viaja",
        bio = "Viajera apasionada · 23 países visitados · Compartiendo el mundo un lugar a la vez",
        email = "valentina@correo.com",
        location = "Bogotá, Colombia",
        notificationPreferences = ProfileNotificationPreferences(
            newFollowers = true,
            reviewComments = true,
            likesReceived = false
        ),
        initials = "VG",
        isVerified = true,
        stats = UserStats(
            places = 47,
            reviews = 38,
            followers = "1.2k"
        )
    )

    val homeCategories = listOf(
        HomeCategory(HomeCategoryId.NATURE, R.string.home_category_nature),
        HomeCategory(HomeCategoryId.GASTRONOMY, R.string.home_category_gastronomy),
        HomeCategory(HomeCategoryId.GETAWAYS, R.string.home_category_getaways),
        HomeCategory(HomeCategoryId.CULTURE, R.string.home_category_culture),
        HomeCategory(HomeCategoryId.ADVENTURE, R.string.home_category_adventure)
    )

    val homeFeaturedPlans = listOf(
        HomeFeaturedPlan(
            id = "featured_cerro_monserrate",
            placeId = "cerro_monserrate",
            categoryId = HomeCategoryId.NATURE,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 312
        ),
        HomeFeaturedPlan(
            id = "featured_valle_cocora",
            placeId = "valle_del_cocora",
            categoryId = HomeCategoryId.NATURE,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 287
        ),
        HomeFeaturedPlan(
            id = "featured_cano_cristales",
            placeId = "cano_cristales",
            categoryId = HomeCategoryId.NATURE,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 264
        ),
        HomeFeaturedPlan(
            id = "featured_gastronomy_mirador",
            placeId = "mirador_cafetero",
            categoryId = HomeCategoryId.GASTRONOMY,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 198
        ),
        HomeFeaturedPlan(
            id = "featured_getaway_costero",
            placeId = "paseo_costero",
            categoryId = HomeCategoryId.GETAWAYS,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 176
        ),
        HomeFeaturedPlan(
            id = "featured_culture_cartagena",
            placeId = "cartagena_amurallada",
            categoryId = HomeCategoryId.CULTURE,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 493
        ),
        HomeFeaturedPlan(
            id = "featured_adventure_penol",
            placeId = "piedra_del_penol",
            categoryId = HomeCategoryId.ADVENTURE,
            badgeRes = R.string.home_featured_badge,
            initialLikeCount = 356
        )
    )

    val homeReviews = listOf(
        HomeReview(
            id = "home_review_monserrate",
            placeId = "cerro_monserrate",
            authorNameRes = R.string.home_review_valentina_name,
            authorHandleRes = R.string.home_review_valentina_handle,
            authorInitialsRes = R.string.home_review_valentina_initials,
            dateRes = R.string.home_review_valentina_date,
            bodyRes = R.string.home_review_valentina_body,
            rating = 5,
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1700526032306-e93c9254f594?w=300&h=300&fit=crop&auto=format",
                "https://images.unsplash.com/photo-1720067392108-89b9485aa090?w=300&h=300&fit=crop&auto=format"
            ),
            initialLikeCount = 214,
            initialCommentCount = 18
        ),
        HomeReview(
            id = "home_review_cocora",
            placeId = "valle_del_cocora",
            authorNameRes = R.string.home_review_miguel_name,
            authorHandleRes = R.string.home_review_miguel_handle,
            authorInitialsRes = R.string.home_review_miguel_initials,
            dateRes = R.string.home_review_miguel_date,
            bodyRes = R.string.home_review_miguel_body,
            rating = 5,
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1778188985186-25a9d5f7d2b8?w=300&h=300&fit=crop&auto=format",
                "https://images.unsplash.com/photo-1776127297381-7b6e64ce1f70?w=300&h=300&fit=crop&auto=format",
                "https://images.unsplash.com/photo-1631134950135-ab17ef088779?w=300&h=300&fit=crop&auto=format"
            ),
            initialLikeCount = 387,
            initialCommentCount = 32
        ),
        HomeReview(
            id = "home_review_paseo_costero",
            placeId = "paseo_costero",
            authorNameRes = R.string.home_review_laura_name,
            authorHandleRes = R.string.home_review_laura_handle,
            authorInitialsRes = R.string.home_review_laura_initials,
            dateRes = R.string.home_review_laura_date,
            bodyRes = R.string.home_review_laura_body,
            rating = 5,
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1602608099803-96718a589bb3?w=300&h=300&fit=crop&auto=format",
                "https://images.unsplash.com/photo-1620095361505-e55b12a8dbd0?w=300&h=300&fit=crop&auto=format"
            ),
            initialLikeCount = 156,
            initialCommentCount = 11
        ),
        HomeReview(
            id = "home_review_cartagena",
            placeId = "cartagena_amurallada",
            authorNameRes = R.string.home_review_andres_name,
            authorHandleRes = R.string.home_review_andres_handle,
            authorInitialsRes = R.string.home_review_andres_initials,
            dateRes = R.string.home_review_andres_date,
            bodyRes = R.string.home_review_andres_body,
            rating = 5,
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1780403266607-ea014a351477?w=300&h=300&fit=crop&auto=format",
                "https://images.unsplash.com/photo-1770808564556-7bc511b893a2?w=300&h=300&fit=crop&auto=format"
            ),
            initialLikeCount = 493,
            initialCommentCount = 47
        )
    )

    val sampleReview = Review(
        usuario = "@viajero99",
        comentario = "Increíble lugar, volvería mil veces.",
        rating = 5,
        fecha = "12/08/2026"
    )

    val notifications = listOf(
        Notification(
            id = 1,
            username = "Carlos Medina",
            message = "comenzó a seguirte.",
            detail = "847 seguidores · 22 reseñas",
            time = "Hace 5 min"
        ),
        Notification(
            id = 2,
            username = "Laura Sánchez",
            message = "comentó tu reseña de Monserrate.",
            detail = "\"Totalmente de acuerdo! El atardecer es...\"",
            time = "Hace 23 min"
        ),
        Notification(
            id = 3,
            username = "Andrés Torres",
            message = "y 14 personas más les gustó tu reseña.",
            detail = "Parque Tayrona · 4.8",
            time = "Hace 1 h"
        ),
        Notification(
            id = 4,
            username = "Sofía Herrera",
            message = "le gustó tu foto en Cartagena.",
            detail = "Centro Histórico de Cartagena",
            time = "Hace 5 h"
        ),
        Notification(
            id = 5,
            username = "WaySpot",
            message = "te recomienda un nuevo destino.",
            detail = "Ciudad Perdida, Sierra Nevada · Basado en tus visitas",
            time = "Hace 3 h"
        ),
        Notification(
            id = 6,
            username = "Playa Blanca",
            message = "actualizó su información.",
            detail = "Horarios de acceso modificados · Guardado en tu lista",
            time = "Hace 1 día"
        )
    )

    val reviews = listOf(
        Review(
            usuario = "Valentina García",
            comentario = "Una experiencia increíble. La vista y el recorrido valen completamente la pena.",
            rating = 5,
            fecha = "12 ago 2026"
        ),
        Review(
            usuario = "Valentina García",
            comentario = "Un lugar que definitivamente volvería a visitar.",
            rating = 4,
            fecha = "3 jul 2026"
        )
    )

    val listReviews = listOf(
        Review(
            usuario = "@valentina_viaja",
            placeTitle = "Cerro Monserrate",
            location = "Bogotá, Colombia",
            imageUrl = "https://images.unsplash.com/photo-1720067392108-89b9485aa090?w=800&h=520&fit=crop&auto=format",
            comentario = "Una experiencia espiritual única. La vista desde la cima al atardecer es simplemente...",
            rating = 5,
            fecha = "12 ago 2026"
        ),
        Review(
            usuario = "@valentina_viaja",
            placeTitle = "Machu Picchu",
            location = "Cusco, Perú",
            imageUrl = "https://images.unsplash.com/photo-1531065208531-4036c0dba3ca?q=80&w=1200&auto=format&fit=crop",
            comentario = "Una maravilla del mundo que supera todas las expectativas. El amanecer es...",
            rating = 5,
            fecha = "3 jul 2026"
        )
    )
}
