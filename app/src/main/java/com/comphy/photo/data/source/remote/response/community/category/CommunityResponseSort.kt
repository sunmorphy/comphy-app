package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class CommunityResponseSort(

	@SerializedName("unsorted")
	val unsorted: Boolean,

	@SerializedName("sorted")
	val sorted: Boolean,

	@SerializedName("empty")
	val empty: Boolean
)