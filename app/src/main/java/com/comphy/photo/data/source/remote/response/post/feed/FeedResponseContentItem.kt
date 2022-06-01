package com.comphy.photo.data.source.remote.response.post.feed

import android.os.Parcelable
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedResponseContentItem(
    @SerializedName("aperture")
    val aperture: String? = null,

    @SerializedName("camera")
    val camera: String? = null,

    @SerializedName("categoryCommunity")
    val categoryCommunity: CategoryCommunityResponseContentItem,

    @SerializedName("community")
    val community: String? = null,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("flash")
    val flash: String? = null,

    @SerializedName("id")
    val id: String,

    @SerializedName("isFollowed")
    var isFollowed: Int,

    @SerializedName("iso")
    val iso: String? = null,

    @SerializedName("lens")
    val lens: String? = null,

    @SerializedName("liked")
    var liked: Boolean,

    @SerializedName("linkPhoto")
    val linkPhoto: String? = null,

    @SerializedName("linkVideo")
    val linkVideo: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("orientationType")
    val orientationType: Int,

    @SerializedName("postSaved")
    var postSaved: Boolean,

    @SerializedName("public")
    val `public`: Boolean,

    @SerializedName("shutterSpeed")
    val shutterSpeed: String? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("totalComments")
    val totalComments: Int,

    @SerializedName("totalLikes")
    val totalLikes: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null,

    @SerializedName("userPost")
    val userPost: UserPost
) : Parcelable