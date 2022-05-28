package com.comphy.photo.data.source.remote.response.user.following


import com.google.gson.annotations.SerializedName

data class Pageable(
    @SerializedName("offset")
    val offset: Int = 0,

    @SerializedName("pageNumber")
    val pageNumber: Int = 0,

    @SerializedName("pageSize")
    val pageSize: Int = 0,

    @SerializedName("paged")
    val paged: Boolean = false,

    @SerializedName("sort")
    val sort: Sort = Sort(),

    @SerializedName("unpaged")
    val unpaged: Boolean = false
)