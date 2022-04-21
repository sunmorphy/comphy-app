package com.comphy.photo.data.model.response.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("data")
    var responseData: Data? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("Status")
    var status: String? = null
)