package com.comphy.photo.di

import com.comphy.photo.data.remote.ApiClient
import com.comphy.photo.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiClient.instance
    }

//    @Provides
//    @Singleton
//    fun provideNetworkStateManager(application: Application): NetworkStateManager {
//        return NetworkStateManagerImpl(application)
//    }
}