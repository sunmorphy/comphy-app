package com.comphy.photo.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("Status")
    val status: String,

    @SerializedName("data")
    val data: Any? = null,

    @SerializedName("message")
    val message: String
)
