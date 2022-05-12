package com.comphy.photo.data.source.remote.response.job.list

import com.google.gson.annotations.SerializedName

data class JobResponseSort(

	@SerializedName("unsorted")
	val unsorted: Boolean? = null,

	@SerializedName("sorted")
	val sorted: Boolean? = null,

	@SerializedName("empty")
	val empty: Boolean? = null
)