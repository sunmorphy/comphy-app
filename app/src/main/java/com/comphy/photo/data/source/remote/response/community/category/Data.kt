package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("number")
	val number: Int,

	@field:SerializedName("last")
	val last: Boolean,

	@field:SerializedName("size")
	val size: Int,

	@field:SerializedName("numberOfElements")
	val numberOfElements: Int,

	@field:SerializedName("totalPages")
	val totalPages: Int,

	@field:SerializedName("pageable")
	val pageable: Pageable,

	@field:SerializedName("sort")
	val sort: Sort,

	@field:SerializedName("content")
	val content: List<Any?>? = null,

	@field:SerializedName("first")
	val first: Boolean,

	@field:SerializedName("totalElements")
	val totalElements: Int,

	@field:SerializedName("empty")
	val empty: Boolean
)