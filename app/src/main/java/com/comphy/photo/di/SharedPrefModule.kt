package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.data.source.local.sharedpref.auth.UserLogin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    @Singleton
    fun provideAuthPref(application: Application): UserLogin {
        return UserLogin(application)
    }
}