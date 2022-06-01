package com.comphy.photo.data.source.remote.response.community.follow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowCommunityResponseContentItemX(
    @SerializedName("banned")
    val banned: Boolean,

    @SerializedName("community")
    val community: Community,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null
) : Parcelable