package com.comphy.photo.data.source.remote.response.user.detail

import com.google.gson.annotations.SerializedName

data class UserDataBody(

	@SerializedName("numberPhone")
	val numberPhone: String? = null,

	@SerializedName("description")
	val description: String,

	@SerializedName("location")
	val location: String,

	@SerializedName("fullname")
	val fullname: String,

	@SerializedName("id")
	val id: Int,

	@SerializedName("job")
	val job: String,

	@SerializedName("socialMedia")
	val socialMedia: String? = null
)