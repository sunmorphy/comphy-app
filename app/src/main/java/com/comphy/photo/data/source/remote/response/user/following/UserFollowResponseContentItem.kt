package com.comphy.photo.data.source.remote.response.user.following

import com.comphy.photo.data.source.remote.response.post.feed.UserPost
import com.google.gson.annotations.SerializedName

data class UserFollowResponseContentItem(
    @SerializedName("created_date")
    val createdDate: Long = 0,
    @SerializedName("deleted_date")
    val deletedDate: Any? = null,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("updated_date")
    val updatedDate: String = "",
    @SerializedName("userFollowed")
    val userFollow: UserPost
)