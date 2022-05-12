package com.comphy.photo.data.source.remote.response.post.feed

import com.google.gson.annotations.SerializedName

data class FeedResponse(

    @SerializedName("Status")
	val status: String,

    @SerializedName("data")
	val feedResponseData: FeedResponseData? = null,

    @SerializedName("message")
	val message: String
)