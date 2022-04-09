package com.comphy.photo.data.model.response.register


import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)