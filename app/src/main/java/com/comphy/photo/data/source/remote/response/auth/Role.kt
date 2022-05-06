package com.comphy.photo.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("created_date")
    val createdDate: Long,
    @SerializedName("deleted_date")
    val deletedDate: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_date")
    val updatedDate: Long
)