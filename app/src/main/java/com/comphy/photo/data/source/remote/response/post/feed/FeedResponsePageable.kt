package com.comphy.photo.data.source.remote.response.post.feed


import com.google.gson.annotations.SerializedName

data class FeedResponsePageable(
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("pageNumber")
    val pageNumber: Int = 0,
    @SerializedName("pageSize")
    val pageSize: Int = 0,
    @SerializedName("paged")
    val paged: Boolean = false,
    @SerializedName("sort")
    val sort: FeedResponseSort,
    @SerializedName("unpaged")
    val unpaged: Boolean = false
)