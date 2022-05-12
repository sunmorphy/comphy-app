package com.comphy.photo.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("data")
    var responseAuthData: AuthResponseData? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("Status")
    var status: String? = null
)