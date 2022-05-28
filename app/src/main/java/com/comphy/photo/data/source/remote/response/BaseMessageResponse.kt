package com.comphy.photo.data.source.remote.response

import com.google.gson.JsonNull
import com.google.gson.annotations.SerializedName

data class BaseMessageResponse(
    @SerializedName("Status")
    val status: String,

    @SerializedName("data")
    val data: JsonNull? = null,

    @SerializedName("message")
    val message: String
)
