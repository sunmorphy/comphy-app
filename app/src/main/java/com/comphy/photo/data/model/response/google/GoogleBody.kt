package com.comphy.photo.data.model.response.google

import com.google.gson.annotations.SerializedName

data class GoogleBody(
    @SerializedName("username")
    val email: String,
    @SerializedName("token")
    val token: String
)
