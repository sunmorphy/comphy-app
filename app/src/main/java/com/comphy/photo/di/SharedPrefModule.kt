package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.local.sharedpref.search.SearchHistory
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
    fun provideAuthPref(application: Application): UserAuth {
        return UserAuth(application)
    }

    @Provides
    @Singleton
    fun provideSearchPref(application: Application): SearchHistory {
        return SearchHistory(application)
    }
}