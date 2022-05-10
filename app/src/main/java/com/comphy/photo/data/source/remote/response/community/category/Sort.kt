package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class Sort(

	@field:SerializedName("unsorted")
	val unsorted: Boolean,

	@field:SerializedName("sorted")
	val sorted: Boolean,

	@field:SerializedName("empty")
	val empty: Boolean
)