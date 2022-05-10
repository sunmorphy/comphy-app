package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class Pageable(

	@field:SerializedName("paged")
	val paged: Boolean,

	@field:SerializedName("pageNumber")
	val pageNumber: Int,

	@field:SerializedName("offset")
	val offset: Int,

	@field:SerializedName("pageSize")
	val pageSize: Int,

	@field:SerializedName("unpaged")
	val unpaged: Boolean,

	@field:SerializedName("sort")
	val sort: Sort
)