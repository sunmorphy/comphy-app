package com.comphy.photo.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class User(

	@SerializedName("deleted_date")
	val deletedDate: String? = null,

	@SerializedName("numberPhone")
	val numberPhone: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("location")
	val location: String? = null,

	@SerializedName("created_date")
	val createdDate: String? = null,

	@SerializedName("updated_date")
	val updatedDate: String? = null,

	@SerializedName("fullname")
	val fullname: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("job")
	val job: String? = null,

	@SerializedName("socialMedia")
	val socialMedia: String? = null,

	@SerializedName("username")
	val username: String? = null
)
