package com.comphy.photo.data.source.remote.response.user

import com.google.gson.annotations.SerializedName

data class UserData(

	@SerializedName("numberPhone")
	var numberPhone: Any? = null,

	@SerializedName("lengthFollowers")
	var lengthFollowers: Int? = null,

	@SerializedName("description")
	var description: Any? = null,

	@SerializedName("subscription")
	var subscription: Subscription? = null,

	@SerializedName("lengthFollowing")
	var lengthFollowing: Int? = null,

	@SerializedName("socialMedia")
	var socialMedia: Any? = null,

	@SerializedName("experiences")
	var experiences: List<Any?>? = null,

	@SerializedName("deleted_date")
	var deletedDate: Any? = null,

	@SerializedName("location")
	var location: Any? = null,

	@SerializedName("created_date")
	var createdDate: String? = null,

	@SerializedName("updated_date")
	var updatedDate: String? = null,

	@SerializedName("fullname")
	var fullname: String? = null,

	@SerializedName("id")
	var id: Int? = null,

	@SerializedName("job")
	var job: Any? = null,

	@SerializedName("username")
	var username: String? = null
)