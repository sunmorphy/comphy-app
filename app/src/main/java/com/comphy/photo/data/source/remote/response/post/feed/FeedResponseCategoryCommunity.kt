package com.comphy.photo.data.source.remote.response.post.feed


import com.google.gson.annotations.SerializedName

data class FeedResponseCategoryCommunity(
    @SerializedName("created_date")
    val createdDate: Long? = null,
    @SerializedName("deleted_date")
    val deletedDate: String? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_date")
    val updatedDate: String? = null
)