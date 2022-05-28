package com.comphy.photo.data.source.remote.response.user.following


import com.google.gson.annotations.SerializedName

data class UserFollowingResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("Status")
    val status: String = ""
)