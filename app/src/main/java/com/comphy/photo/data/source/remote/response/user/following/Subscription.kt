package com.comphy.photo.data.source.remote.response.user.following


import com.google.gson.annotations.SerializedName

data class Subscription(
    @SerializedName("id")
    val id: Int,

    @SerializedName("limitCreateCommunity")
    val limitCreateCommunity: Int,

    @SerializedName("limitJoinCommunity")
    val limitJoinCommunity: Int
)