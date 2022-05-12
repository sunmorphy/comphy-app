package com.comphy.photo.data.source.remote.response.post.feed

import com.google.gson.annotations.SerializedName

data class FeedResponseSort(

	@SerializedName("unsorted")
	val unsorted: Boolean? = null,

	@SerializedName("sorted")
	val sorted: Boolean? = null,

	@SerializedName("empty")
	val empty: Boolean? = null
)