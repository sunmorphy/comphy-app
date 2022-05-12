package com.comphy.photo.data.source.remote.response.job.list

import com.google.gson.annotations.SerializedName

data class JobResponseData(

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
	val jobResponsePageable: JobResponsePageable? = null,

	@SerializedName("sort")
	val jobResponseSort: JobResponseSort? = null,

	@SerializedName("content")
	val content: List<Any>? = null,

	@SerializedName("first")
	val first: Boolean? = null,

	@SerializedName("totalElements")
	val totalElements: Int? = null,

	@SerializedName("empty")
	val empty: Boolean? = null
)