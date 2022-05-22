package com.comphy.photo.data.source.remote.response.user.detail

import com.google.gson.annotations.SerializedName

data class UserResponseDataSubscription(

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("limitCreateCommunity")
	val limitCreateCommunity: Int? = null,

	@SerializedName("limitJoinCommunity")
	val limitJoinCommunity: Int? = null
)