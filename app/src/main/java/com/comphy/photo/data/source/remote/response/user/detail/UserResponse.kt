package com.comphy.photo.data.source.remote.response.user.detail

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	var userResponseData: UserResponseData? = null,

	@SerializedName("message")
	val message: String
)