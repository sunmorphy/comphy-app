package com.comphy.photo.data.source.remote.response.community.category

import com.google.gson.annotations.SerializedName

data class CommunityResponseContentItem(

	@SerializedName("deleted_date")
	val deletedDate: Any? = null,

	@SerializedName("name")
	val name: String,

	@SerializedName("created_date")
	val createdDate: Long? = null,

	@SerializedName("updated_date")
	val updatedDate: String? = null,

	@SerializedName("id")
	val id: Int
)