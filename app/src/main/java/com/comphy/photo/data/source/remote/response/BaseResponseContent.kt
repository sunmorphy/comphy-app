package com.comphy.photo.data.source.remote.response

import com.comphy.photo.data.source.remote.response.user.following.Pageable
import com.comphy.photo.data.source.remote.response.user.following.Sort
import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class BaseResponseContent(
    @SerializedName("number")
    val number: Int,

    @SerializedName("last")
    val last: Boolean,

    @SerializedName("size")
    val size: Int,

    @SerializedName("numberOfElements")
    val numberOfElements: Int,

    @SerializedName("totalPages")
    val totalPages: Int,

    @SerializedName("pageable")
    val pageable: Pageable,

    @SerializedName("sort")
    val sort: Sort,

    @SerializedName("content")
    val content: JsonArray? = null,

    @SerializedName("first")
    val first: Boolean,

    @SerializedName("totalElements")
    val totalElements: Int,

    @SerializedName("empty")
    val empty: Boolean
)
