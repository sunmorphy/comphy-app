package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class Subscription(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("limitCreateCommunity")
	val limitCreateCommunity: Int? = null,

	@field:SerializedName("limitJoinCommunity")
	val limitJoinCommunity: Int? = null
)