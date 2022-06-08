package com.comphy.photo.data.source.remote.response.community.create

import com.google.gson.annotations.SerializedName

data class CommunityBody(

	@SerializedName("communityType")
	val communityType: String,

	@SerializedName("categoryCommunityId")
	val categoryCommunityId: Int,

	@SerializedName("description")
	val description: String,

	@SerializedName("profilePhotoCommunityLink")
	val profilePhotoCommunityLink: String? = null,

	@SerializedName("communityName")
	val communityName: String,

	@SerializedName("location")
	val location: String,

	@SerializedName("bannerPhotoCommunityLink")
	val bannerPhotoCommunityLink: String? = null,

	@SerializedName("Id")
	val id: Int? = null
)