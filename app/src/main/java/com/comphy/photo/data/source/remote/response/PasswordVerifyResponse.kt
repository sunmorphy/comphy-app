package com.comphy.photo.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PasswordVerifyResponse(
    @SerializedName("data")
    val `data`: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("Status")
    val status: String
)