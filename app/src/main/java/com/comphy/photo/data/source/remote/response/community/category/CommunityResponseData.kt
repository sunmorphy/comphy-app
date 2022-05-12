package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class CommunityResponseData(

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
	val communityResponsePageable: CommunityResponsePageable,

	@SerializedName("sort")
	val communityResponseSort: CommunityResponseSort,

	@SerializedName("content")
	val content: List<Any?>? = null,

	@SerializedName("first")
	val first: Boolean,

	@SerializedName("totalElements")
	val totalElements: Int,

	@SerializedName("empty")
	val empty: Boolean
)