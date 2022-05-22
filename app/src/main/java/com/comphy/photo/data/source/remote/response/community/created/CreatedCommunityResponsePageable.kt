package com.comphy.photo.data.source.remote.response.community.created

import com.google.gson.annotations.SerializedName

data class CreatedCommunityResponsePageable(

	@SerializedName("paged")
	val paged: Boolean,

	@SerializedName("pageNumber")
	val pageNumber: Int,

	@SerializedName("offset")
	val offset: Int,

	@SerializedName("pageSize")
	val pageSize: Int,

	@SerializedName("unpaged")
	val unpaged: Boolean,

	@SerializedName("sort")
	val sort: CreatedCommunityResponseSort
)