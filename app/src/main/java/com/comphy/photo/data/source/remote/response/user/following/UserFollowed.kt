package com.comphy.photo.data.source.remote.response.user.following


import com.google.gson.annotations.SerializedName

data class UserFollowed(
    @SerializedName("created_date")
    val createdDate: Long = 0,
    @SerializedName("deleted_date")
    val deletedDate: Any? = null,
    @SerializedName("description")
    val description: Any? = null,
    @SerializedName("experiences")
    val experiences: List<Any> = listOf(),
    @SerializedName("fullname")
    val fullname: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("job")
    val job: Any? = null,
    @SerializedName("lengthFollowers")
    val lengthFollowers: Int = 0,
    @SerializedName("lengthFollowing")
    val lengthFollowing: Int = 0,
    @SerializedName("location")
    val location: Any? = null,
    @SerializedName("numberPhone")
    val numberPhone: Any? = null,
    @SerializedName("profileBannerLink")
    val profileBannerLink: Any? = null,
    @SerializedName("profilePhotoLink")
    val profilePhotoLink: Any? = null,
    @SerializedName("socialMedia")
    val socialMedia: Any? = null,
    @SerializedName("subscription")
    val subscription: Subscription,
    @SerializedName("updated_date")
    val updatedDate: String = "",
    @SerializedName("username")
    val username: String = ""
)