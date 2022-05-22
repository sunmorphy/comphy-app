package com.comphy.photo.data.source.remote.response.post.feed

import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("data")
    val `data`: FeedResponseData? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("Status")
    val status: String
)