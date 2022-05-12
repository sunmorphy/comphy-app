package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class UpdatePostBody(

	@field:SerializedName("orientationType")
	val orientationType: Int? = null,

	@field:SerializedName("iso")
	val iso: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("linkVideo")
	val linkVideo: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("lens")
	val lens: String? = null,

	@field:SerializedName("community")
	val community: Community? = null,

	@field:SerializedName("categoryCommunity")
	val categoryCommunity: CategoryCommunity? = null,

	@field:SerializedName("linkPhoto")
	val linkPhoto: String? = null,

	@field:SerializedName("shutterSpeed")
	val shutterSpeed: String? = null,

	@field:SerializedName("deleted_date")
	val deletedDate: String? = null,

	@field:SerializedName("aperture")
	val aperture: String? = null,

	@field:SerializedName("public")
	val jsonMemberPublic: Boolean? = null,

	@field:SerializedName("userPost")
	val userPost: UserPost? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("camera")
	val camera: String? = null,

	@field:SerializedName("flash")
	val flash: String? = null
)