package com.comphy.photo.data.source.remote.response.post.create

import com.google.gson.annotations.SerializedName

data class PostBody(

	@SerializedName("postId")
	val postId: String? = null,

	@SerializedName("orientationType")
	val orientationType: Int? = null,

	@SerializedName("iso")
	val iso: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("linkVideo")
	val linkVideo: String? = null,

	@SerializedName("title")
	val title: String,

	@SerializedName("lens")
	val lens: String? = null,

	@SerializedName("linkPhoto")
	val linkPhoto: String? = null,

	@SerializedName("shutterSpeed")
	val shutterSpeed: String? = null,

	@SerializedName("aperture")
	val aperture: String? = null,

	@SerializedName("categoryId")
	val categoryId: Int,

	@SerializedName("location")
	val location: String? = null,

	@SerializedName("camera")
	val camera: String? = null,

	@SerializedName("communityId")
	val communityId: Int? = null,

	@SerializedName("flash")
	val flash: String? = null
)