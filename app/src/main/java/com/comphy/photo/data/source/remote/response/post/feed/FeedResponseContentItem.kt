package com.comphy.photo.data.source.remote.response.post.feed

import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.google.gson.annotations.SerializedName

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
    val isFollowed: Int,

    @SerializedName("iso")
    val iso: String? = null,

    @SerializedName("lens")
    val lens: String? = null,

    @SerializedName("liked")
    val liked: Boolean = false,

    @SerializedName("linkPhoto")
    val linkPhoto: String? = null,

    @SerializedName("linkVideo")
    val linkVideo: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("orientationType")
    val orientationType: Int,

    @SerializedName("postSaved")
    val postSaved: Boolean,

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
)