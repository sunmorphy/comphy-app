package com.comphy.photo.data.source.remote.response.user.location

import com.google.gson.annotations.SerializedName

data class UserCityResponseData(

	@SerializedName("cities")
	val cities: List<String>
)