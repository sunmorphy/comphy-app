package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.data.local.ComphyDatabase
import com.comphy.photo.data.local.location.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): ComphyDatabase {
        return ComphyDatabase.instance(application)
    }

    @Provides
    @Singleton
    fun provideLocationDao(comphyDatabase: ComphyDatabase): LocationDao {
        return comphyDatabase.locationDao()
    }

}