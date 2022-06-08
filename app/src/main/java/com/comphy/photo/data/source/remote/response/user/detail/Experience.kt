package com.comphy.photo.data.source.remote.response.user.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Experience(
    @SerializedName("company")
    var company: String,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("position")
    var position: String,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("years")
    var years: String
) : Parcelable