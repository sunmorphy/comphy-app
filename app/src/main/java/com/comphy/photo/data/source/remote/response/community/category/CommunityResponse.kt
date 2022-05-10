package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class CommunityResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	val data: Data? = null,

	@SerializedName("message")
	val message: String
)