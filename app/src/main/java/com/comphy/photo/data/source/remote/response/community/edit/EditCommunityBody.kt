package com.comphy.photo.data.source.remote.response.community.edit

import com.google.gson.annotations.SerializedName

data class EditCommunityBody(

	@SerializedName("communityType")
	val communityType: String,

	@SerializedName("description")
	val description: String,

	@SerializedName("profilePhotoCommunityLink")
	val profilePhotoCommunityLink: String? = null,

	@SerializedName("communityName")
	val communityName: String,

	@SerializedName("location")
	val location: String,

	@SerializedName("bannerPhotoCommunityLink")
	val bannerPhotoCommunityLink: String? = null
)