package com.comphy.photo.data.source.remote.response.community.created

import com.google.gson.annotations.SerializedName

data class CreatedCommunityResponseData(

	@SerializedName("number")
	val number: Int,

	@SerializedName("last")
	val last: Boolean,

	@SerializedName("size")
	val size: Int,

	@SerializedName("numberOfElements")
	val numberOfElements: Int,

	@SerializedName("totalPages")
	val totalPages: Int,

	@SerializedName("pageable")
	val pageable: CreatedCommunityResponsePageable,

	@SerializedName("sort")
	val sort: CreatedCommunityResponseSort,

	@SerializedName("content")
	val content: List<CreatedCommunityResponseContent>? = null,

	@SerializedName("first")
	val first: Boolean,

	@SerializedName("totalElements")
	val totalElements: Int,

	@SerializedName("empty")
	val empty: Boolean
)