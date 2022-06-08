package com.comphy.photo.data.source.remote.response.community.join

import android.os.Parcelable
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinedCommunityResponseContentItem(
    @SerializedName("banned")
    val banned: Boolean,

    @SerializedName("community")
    val community: FollowCommunityResponseContentItem,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null
) : Parcelable