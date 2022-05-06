package com.comphy.photo.di

import android.app.Application
import com.comphy.photo.data.source.local.room.ComphyDatabase
import com.comphy.photo.data.source.local.room.location.LocationDao
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