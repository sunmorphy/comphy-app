package com.comphy.photo.data.source.remote.response.post.update

import com.google.gson.annotations.SerializedName

data class Community(

	@field:SerializedName("deleted_date")
	val deletedDate: String? = null,

	@field:SerializedName("communityType")
	val communityType: String? = null,

	@field:SerializedName("userJoinedCommunities")
	val userJoinedCommunities: List<UserJoinedCommunitiesItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("profilePhotoCommunityLink")
	val profilePhotoCommunityLink: String? = null,

	@field:SerializedName("communityName")
	val communityName: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("bannerPhotoCommunityLink")
	val bannerPhotoCommunityLink: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("categoryCommunity")
	val categoryCommunity: CategoryCommunity? = null
)