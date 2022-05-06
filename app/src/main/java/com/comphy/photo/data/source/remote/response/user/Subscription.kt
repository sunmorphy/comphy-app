package com.comphy.photo.data.source.remote.response.user

import com.google.gson.annotations.SerializedName

data class Subscription(

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("limitCreateCommunity")
	val limitCreateCommunity: Int? = null,

	@SerializedName("limitJoinCommunity")
	val limitJoinCommunity: Int? = null
)