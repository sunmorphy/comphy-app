package com.comphy.photo.data.model.response.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("data")
    val responseData: Data?,
    @SerializedName("message")
    val message: String,
    @SerializedName("Status")
    val status: String
)