package com.comphy.photo.data.source.remote.response.post.create

import com.google.gson.annotations.SerializedName

data class CreatePostResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	val data: Any? = null,

	@SerializedName("message")
	val message: String
)