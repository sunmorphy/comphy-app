package com.comphy.photo.data.source.remote.response.community.created

import com.google.gson.annotations.SerializedName

data class CreatedCommunityResponseSort(

	@SerializedName("unsorted")
	val unsorted: Boolean,

	@SerializedName("sorted")
	val sorted: Boolean,

	@SerializedName("empty")
	val empty: Boolean
)