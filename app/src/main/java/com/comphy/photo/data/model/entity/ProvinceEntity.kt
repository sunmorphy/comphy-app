package com.comphy.photo.data.model.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProvinceWithRegency(
    @Embedded
    val province: ProvinceEntity,

    @Relation(
        parentColumn = "province_id",
        entityColumn = "province_source_id"
    )
    val regencies: List<RegencyEntity>

) : Parcelable

@Entity(tableName = "province")
@Parcelize
data class ProvinceEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "province_id")
    var provinceId: Long? = null,

    @ColumnInfo(name = "province_name")
    val province: String

) : Parcelable

@Entity(tableName = "regency")
@Parcelize
data class RegencyEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "regency_id")
    var regencyId: Long? = null,

    @ColumnInfo(name = "province_source_id")
    val provinceSourceId: Long,

    @ColumnInfo(name = "regency_name")
    val regencyName: String

) : Parcelable