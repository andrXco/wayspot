package com.example.wayspot.data.injection

import com.example.wayspot.data.datasource.PlaceRemoteDataSource
import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.datasource.implementation.firestore.PlaceFirestoreDataSourceImplementation
import com.example.wayspot.data.datasource.implementation.firestore.UserFirestoreDataSourceImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindPlaceRemoteDataSource(
        implementation: PlaceFirestoreDataSourceImplementation
    ): PlaceRemoteDataSource

    @Binds
    abstract fun bindUserRemoteDataSource(
        implementation: UserFirestoreDataSourceImplementation
    ): UserRemoteDataSource
}
