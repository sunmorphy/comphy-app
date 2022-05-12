package com.comphy.photo.data.source.remote.response.post.feed

import com.google.gson.annotations.SerializedName

data class FeedResponseData(

    @SerializedName("number")
	val number: Int? = null,

    @SerializedName("last")
	val last: Boolean? = null,

    @SerializedName("size")
	val size: Int? = null,

    @SerializedName("numberOfElements")
	val numberOfElements: Int? = null,

    @SerializedName("totalPages")
	val totalPages: Int? = null,

    @SerializedName("pageable")
	val feedResponsePageable: FeedResponsePageable? = null,

    @SerializedName("sort")
	val feedResponseSort: FeedResponseSort? = null,

    @SerializedName("content")
	val content: List<Any?>? = null,

    @SerializedName("first")
	val first: Boolean? = null,

    @SerializedName("totalElements")
	val totalElements: Int? = null,

    @SerializedName("empty")
	val empty: Boolean? = null
)