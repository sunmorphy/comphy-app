package com.comphy.photo.data.source.remote.response.location

import com.google.gson.annotations.SerializedName

data class CityResponseData(

	@SerializedName("cities")
	val cities: List<String>
)