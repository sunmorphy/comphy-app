package com.comphy.photo.data.source.local.room.location

import androidx.room.*
import com.comphy.photo.data.source.local.entity.ProvinceEntity
import com.comphy.photo.data.source.local.entity.ProvinceWithRegency
import com.comphy.photo.data.source.local.entity.RegencyEntity

@Dao
interface LocationDao {

    @Transaction
    @Query("SELECT * FROM province")
    fun getLocation(): List<ProvinceWithRegency>

    @Query("SeLECt * frOm regency")
    fun getRegencies(): List<RegencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinceEntities: ProvinceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegency(provinceEntities: RegencyEntity)

}