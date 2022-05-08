package com.comphy.photo.data.local.location

import androidx.room.*
import com.comphy.photo.data.model.entity.ProvinceEntity
import com.comphy.photo.data.model.entity.ProvinceWithRegency
import com.comphy.photo.data.model.entity.RegencyEntity

@Dao
interface LocationDao {

    @Transaction
    @Query("SELECT * FROM province")
    fun getLocation(): List<ProvinceWithRegency>

    @Query("SeLECt * from regency")
    fun getRegencies(): List<RegencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinceEntities: ProvinceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegency(provinceEntities: RegencyEntity)

}