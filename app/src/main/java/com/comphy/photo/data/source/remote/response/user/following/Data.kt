package com.comphy.photo.data.source.remote.response.user.following


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("content")
    val content: List<UserFollowResponseContentItem> = listOf(),
    @SerializedName("empty")
    val empty: Boolean = false,
    @SerializedName("first")
    val first: Boolean = false,
    @SerializedName("last")
    val last: Boolean = false,
    @SerializedName("number")
    val number: Int = 0,
    @SerializedName("numberOfElements")
    val numberOfElements: Int = 0,
    @SerializedName("pageable")
    val pageable: Pageable = Pageable(),
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("sort")
    val sort: Sort = Sort(),
    @SerializedName("totalElements")
    val totalElements: Int = 0,
    @SerializedName("totalPages")
    val totalPages: Int = 0
)