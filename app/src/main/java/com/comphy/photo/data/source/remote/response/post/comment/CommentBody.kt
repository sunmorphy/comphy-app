package com.comphy.photo.data.source.remote.response.post.comment

import com.google.gson.annotations.SerializedName

data class CommentBody(
    @SerializedName("comment")
    val comment: String,

    @SerializedName("commentId")
    val commentId: Int? = null,

    @SerializedName("parentId")
    val parentId: Int? = null,

    @SerializedName("postId")
    val postId: String
)