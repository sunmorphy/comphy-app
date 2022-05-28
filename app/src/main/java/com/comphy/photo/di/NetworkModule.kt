package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.ComphyApp
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.client.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(
        application: Application,
        okHttpClient: OkHttpClient
    ): ApiService = ApiClient(okHttpClient).instance((application as ComphyApp).baseUrl())

    @Provides
    fun provideOkHttpClient(
        userAuth: UserAuth,
        application: Application
    ): OkHttpClient = okHttpClient(userAuth, (application as ComphyApp).baseUrl())
}