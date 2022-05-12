package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class UserPost(

	@field:SerializedName("numberPhone")
	val numberPhone: String? = null,

	@field:SerializedName("lengthFollowers")
	val lengthFollowers: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("subscription")
	val subscription: Subscription? = null,

	@field:SerializedName("lengthFollowing")
	val lengthFollowing: Int? = null,

	@field:SerializedName("socialMedia")
	val socialMedia: String? = null,

	@field:SerializedName("deleted_date")
	val deletedDate: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("job")
	val job: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)