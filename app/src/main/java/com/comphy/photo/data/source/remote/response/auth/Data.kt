package com.comphy.photo.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("userId")
    val userId: Int? = null,
    @SerializedName("user")
    val user: User? = null
)