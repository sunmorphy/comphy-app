package com.comphy.photo.data.model.response.login


import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)