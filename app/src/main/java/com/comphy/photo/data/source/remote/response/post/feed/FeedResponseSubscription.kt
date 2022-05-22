package com.comphy.photo.data.source.remote.response.post.feed


import com.google.gson.annotations.SerializedName

data class FeedResponseSubscription(
    @SerializedName("id")
    val id: Int,
    @SerializedName("limitCreateCommunity")
    val limitCreateCommunity: Int,
    @SerializedName("limitJoinCommunity")
    val limitJoinCommunity: Int
)