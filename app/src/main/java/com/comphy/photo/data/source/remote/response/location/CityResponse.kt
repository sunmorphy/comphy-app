package com.comphy.photo.data.source.remote.response.location

import com.google.gson.annotations.SerializedName

data class CityResponse(

    @SerializedName("Status")
	val status: String,

    @SerializedName("data")
	val cityResponseData: CityResponseData,

    @SerializedName("message")
	val message: String
)