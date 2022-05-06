package com.comphy.photo.data.source.remote.response.user

import com.google.gson.annotations.SerializedName

data class UserDataBody(

	@SerializedName("numberPhone")
	val numberPhone: String? = null,

	@SerializedName("lengthFollowers")
	val lengthFollowers: Int? = null,

	@SerializedName("description")
	val description: String,

	@SerializedName("subscription")
	val subscription: Subscription? = null,

	@SerializedName("lengthFollowing")
	val lengthFollowing: Int? = null,

	@SerializedName("socialMedia")
	val socialMedia: String? = null,

	@SerializedName("location")
	val location: String,

	@SerializedName("fullname")
	val fullname: String,

	@SerializedName("id")
	val id: Int,

	@SerializedName("job")
	val job: String,

	@SerializedName("username")
	val username: String? = null
)