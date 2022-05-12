package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class UserJoinedCommunitiesItem(

	@field:SerializedName("deleted_date")
	val deletedDate: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("banned")
	val banned: Boolean? = null
)