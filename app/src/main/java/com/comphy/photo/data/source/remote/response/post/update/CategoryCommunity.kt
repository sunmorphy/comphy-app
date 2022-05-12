package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class CategoryCommunity(

	@SerializedName("deleted_date")
	val deletedDate: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("created_date")
	val createdDate: String? = null,

	@SerializedName("updated_date")
	val updatedDate: String? = null,

	@SerializedName("id")
	val id: Int? = null
)