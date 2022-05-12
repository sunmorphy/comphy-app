package com.comphy.photo.data.source.remote.response.post.feed

import com.google.gson.annotations.SerializedName

data class FeedResponsePageable(

	@SerializedName("paged")
	val paged: Boolean? = null,

	@SerializedName("pageNumber")
	val pageNumber: Int? = null,

	@SerializedName("offset")
	val offset: Int? = null,

	@SerializedName("pageSize")
	val pageSize: Int? = null,

	@SerializedName("unpaged")
	val unpaged: Boolean? = null,

	@SerializedName("sort")
	val feedResponseSort: FeedResponseSort? = null
)