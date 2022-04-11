package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.ComphyApp
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
    fun provideApiService(application: Application): ApiService {
        return ApiClient.instance((application as ComphyApp).baseUrl())
    }
}