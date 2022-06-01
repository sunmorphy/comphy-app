package com.comphy.photo.data.source.remote.response.post.comment

import android.os.Parcelable
import com.comphy.photo.data.source.remote.response.post.feed.UserPost
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentResponseContentItem(
    @SerializedName("childrenComment")
    val secondChildComment: List<SecondChildComment>,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("user")
    val user: UserPost
) : Parcelable