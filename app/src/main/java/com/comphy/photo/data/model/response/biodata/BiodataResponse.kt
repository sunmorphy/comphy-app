package com.comphy.photo.data.model.response.biodata

import com.comphy.photo.data.model.response.auth.Data
import com.google.gson.annotations.SerializedName

data class BiodataResponse (
    @SerializedName("data")
    var responseData: Data? = null,
    @SerializedName("message")
    val message: String
)