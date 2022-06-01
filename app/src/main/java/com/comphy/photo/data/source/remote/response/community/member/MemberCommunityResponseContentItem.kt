package com.comphy.photo.data.source.remote.response.community.member

import com.comphy.photo.data.source.remote.response.post.feed.UserPost
import com.google.gson.annotations.SerializedName

data class MemberCommunityResponseContentItem(
    @SerializedName("banned")
    val banned: Boolean = false,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("user")
    val user: UserPost
)