package com.comphy.photo.data.source.remote.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	var userData: UserData? = null,

	@SerializedName("message")
	val message: String
)