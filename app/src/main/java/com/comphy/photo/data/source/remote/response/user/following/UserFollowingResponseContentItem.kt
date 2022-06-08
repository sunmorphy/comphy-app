package com.comphy.photo.data.source.remote.response.user.following

import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.google.gson.annotations.SerializedName

data class UserFollowingResponseContentItem(
    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("userFollowed")
    val userFollow: UserResponseData
)