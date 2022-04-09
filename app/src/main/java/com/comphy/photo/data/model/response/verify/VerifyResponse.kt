package com.comphy.photo.data.model.response.verify


import com.google.gson.annotations.SerializedName

data class VerifyResponse(
    @SerializedName("data")
    val `data`: Any,
    @SerializedName("message")
    val message: String,
    @SerializedName("Status")
    val status: String
)