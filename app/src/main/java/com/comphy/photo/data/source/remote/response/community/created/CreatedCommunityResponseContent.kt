package com.comphy.photo.data.source.remote.response.community.created

import com.google.gson.annotations.SerializedName

data class CreatedCommunityResponseContent(

    @SerializedName("deleted_date")
    val deletedDate: Any? = null,

    @SerializedName("communityType")
    val communityType: String,

    @SerializedName("amountJoinedCommunity")
    val amountJoinedCommunity: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("profilePhotoCommunityLink")
    val profilePhotoCommunityLink: Any? = null,

    @SerializedName("communityName")
    val communityName: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("bannerPhotoCommunityLink")
    val bannerPhotoCommunityLink: Any? = null,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("categoryCommunity")
    val categoryCommunity: CreatedCommunityResponseCategory
)