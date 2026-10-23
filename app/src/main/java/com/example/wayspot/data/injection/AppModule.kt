package com.example.wayspot.data.injection

import com.example.wayspot.data.datasource.services.PlaceRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.example.wayspot.data.datasource.services.UserRetrofitService
import javax.inject.Singleton


import com.example.wayspot.data.datasource.services.ReviewRetrofitService

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceRetrofitService(
        retrofit: Retrofit
    ): PlaceRetrofitService {
        return retrofit.create(PlaceRetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewRetrofitService(
        retrofit: Retrofit
    ): ReviewRetrofitService {
        return retrofit.create(ReviewRetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRetrofitService(
        retrofit: Retrofit
    ): UserRetrofitService {
        return retrofit.create(UserRetrofitService::class.java)
    }
}