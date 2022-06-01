package com.comphy.photo.data.source.local.room.location

import androidx.room.*
import com.comphy.photo.data.source.local.entity.CityEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM city")
    suspend fun getCities(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

}