package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.ComphyApp
import com.comphy.photo.data.source.remote.client.ApiClient
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.client.AuthInterceptor
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
    fun provideApiService(
        application: Application,
        authInterceptor: AuthInterceptor
    ): ApiService {
        return ApiClient(authInterceptor).instance((application as ComphyApp).baseUrl())
    }
}