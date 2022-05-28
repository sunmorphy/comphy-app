package com.comphy.photo.data.source.remote.response.post.comment

import com.comphy.photo.data.source.remote.response.post.feed.UserPost
import com.google.gson.annotations.SerializedName

data class ThirdChildComment(
    @SerializedName("comment")
    val comment: String,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: Any? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("user")
    val user: UserPost
)