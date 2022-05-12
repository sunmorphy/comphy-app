package com.comphy.photo.data.source.remote.response.job.list

import com.google.gson.annotations.SerializedName

data class JobResponsePageable(

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
	val jobResponseSort: JobResponseSort? = null
)