package com.comphy.photo.data.source.remote.response.community.follow

import android.os.Parcelable
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Community(
    @SerializedName("amountJoinedCommunity")
    val amountJoinedCommunity: Int,

    @SerializedName("bannerPhotoCommunityLink")
    val bannerPhotoCommunityLink: String? = null,

    @SerializedName("categoryCommunity")
    val categoryCommunity: CategoryCommunityResponseContentItem,

    @SerializedName("communityName")
    val communityName: String,

    @SerializedName("communityType")
    val communityType: String,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("description")
    val description: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("joined")
    val joined: Boolean,

    @SerializedName("location")
    val location: String,

    @SerializedName("profilePhotoCommunityLink")
    val profilePhotoCommunityLink: String? = null,

    @SerializedName("theAdmin")
    val theAdmin: Boolean,

    @SerializedName("updated_date")
    val updatedDate: String? = null
) : Parcelable