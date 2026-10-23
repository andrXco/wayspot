package com.example.wayspot.data.datasource.implementation

import com.example.wayspot.data.datasource.ReviewRemoteDataSource
import com.example.wayspot.data.datasource.services.ReviewRetrofitService
import com.example.wayspot.data.dto.ReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImplementation @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getReviewsByPlaceId(
        placeId: String
    ): List<ReviewDto> {
        return service.getReviewsByPlaceId(placeId)
    }
}