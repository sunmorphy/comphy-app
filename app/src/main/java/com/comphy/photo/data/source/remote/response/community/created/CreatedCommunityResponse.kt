package com.comphy.photo.data.source.remote.response.community.created

import com.google.gson.annotations.SerializedName

data class CreatedCommunityResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	val data: CreatedCommunityResponseData? = null,

	@SerializedName("message")
	val message: String
)