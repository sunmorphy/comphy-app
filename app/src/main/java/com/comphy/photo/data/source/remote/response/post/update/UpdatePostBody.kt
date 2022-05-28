package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class UpdatePostBody(

	@SerializedName("orientationType")
	val orientationType: Int? = null,

	@SerializedName("iso")
	val iso: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("linkVideo")
	val linkVideo: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("lens")
	val lens: String? = null,

	@SerializedName("community")
	val community: Community? = null,

	@SerializedName("categoryCommunity")
	val categoryCommunity: CategoryCommunity? = null,

	@SerializedName("linkPhoto")
	val linkPhoto: String? = null,

	@SerializedName("shutterSpeed")
	val shutterSpeed: String? = null,

	@SerializedName("deleted_date")
	val deletedDate: String? = null,

	@SerializedName("aperture")
	val aperture: String? = null,

	@SerializedName("public")
	val jsonMemberPublic: Boolean? = null,

	@SerializedName("userPost")
	val userPost: UserPost? = null,

	@SerializedName("location")
	val location: String? = null,

	@SerializedName("created_date")
	val createdDate: String? = null,

	@SerializedName("updated_date")
	val updatedDate: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("camera")
	val camera: String? = null,

	@SerializedName("flash")
	val flash: String? = null
)