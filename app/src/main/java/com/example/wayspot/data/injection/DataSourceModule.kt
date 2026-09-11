package com.example.wayspot.data.injection

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        implementation: UserFirestoreDataSourceImpl
    ): UserRemoteDataSource
}
