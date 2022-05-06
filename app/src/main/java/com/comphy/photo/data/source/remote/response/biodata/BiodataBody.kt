package com.comphy.photo.data.source.remote.response.biodata

import com.google.gson.annotations.SerializedName

data class BiodataBody(

	@SerializedName("fullname")
	val fullname: String,

	@SerializedName("username")
	val username: String,

	@SerializedName("location")
	val location: String,

	@SerializedName("numberPhone")
	val numberPhone: String? = null,

	@SerializedName("job")
	val job: String,

	@SerializedName("description")
	val description: String,

	@SerializedName("socialMedia")
	val socialMedia: String? = null,

	@SerializedName("id")
	val id: Int
)
