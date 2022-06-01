package com.comphy.photo.data.source.remote.response.community.follow

import android.os.Parcelable
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowCommunityResponseContentItem(

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("communityType")
    val communityType: String,

    @SerializedName("amountJoinedCommunity")
    val amountJoinedCommunity: Int,

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

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("categoryCommunity")
    val categoryCommunity: CategoryCommunityResponseContentItem,

    @SerializedName("theAdmin")
    val theAdmin: Boolean,

    @SerializedName("joined")
    val joined: Boolean,

    @SerializedName("communityCode")
    val communityCode: Int? = null
) : Parcelable