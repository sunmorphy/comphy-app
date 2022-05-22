package com.comphy.photo.data.source.remote.response.user.location

import com.google.gson.annotations.SerializedName

data class UserCityResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	val userCityResponseData: UserCityResponseData,

	@SerializedName("message")
	val message: String
)