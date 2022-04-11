package com.comphy.photo.data.model.response.auth

import com.google.gson.annotations.SerializedName

data class AuthBody(
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("token")
    var token: String? = null,
    @SerializedName("username")
    val username: String
)