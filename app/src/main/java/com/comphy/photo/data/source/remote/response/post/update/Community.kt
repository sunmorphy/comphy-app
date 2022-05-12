package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class Community(

	@SerializedName("deleted_date")
	val deletedDate: String? = null,

	@SerializedName("communityType")
	val communityType: String? = null,

	@SerializedName("userJoinedCommunities")
	val userJoinedCommunities: List<UserJoinedCommunitiesItem?>? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("profilePhotoCommunityLink")
	val profilePhotoCommunityLink: String? = null,

	@SerializedName("communityName")
	val communityName: String? = null,

	@SerializedName("location")
	val location: String? = null,

	@SerializedName("bannerPhotoCommunityLink")
	val bannerPhotoCommunityLink: String? = null,

	@SerializedName("created_date")
	val createdDate: String? = null,

	@SerializedName("updated_date")
	val updatedDate: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("categoryCommunity")
	val categoryCommunity: CategoryCommunity? = null
)