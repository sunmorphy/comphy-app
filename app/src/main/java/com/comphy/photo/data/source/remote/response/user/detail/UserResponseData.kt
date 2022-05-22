package com.comphy.photo.data.source.remote.response.user.detail

import com.google.gson.annotations.SerializedName

data class UserResponseData(

    @SerializedName("numberPhone")
    var numberPhone: String? = null,

    @SerializedName("lengthFollowers")
    var lengthFollowers: Int,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("subscription")
    var subscription: UserResponseDataSubscription? = null,

    @SerializedName("lengthFollowing")
    var lengthFollowing: Int,

    @SerializedName("socialMedia")
    var socialMedia: String? = null,

    @SerializedName("experiences")
    var experiences: List<Any?>? = null,

//	@SerializedName("deleted_date")
//	var deletedDate: Any? = null,

    @SerializedName("location")
    var location: String? = null,

//	@SerializedName("created_date")
//	var createdDate: String? = null,
//
//	@SerializedName("updated_date")
//	var updatedDate: String? = null,

    @SerializedName("profilePhotoLink")
    var profileUrl: String? = null,

    @SerializedName("profileBannerLink")
    var bannerUrl: String? = null,

    @SerializedName("fullname")
    var fullname: String,

    @SerializedName("id")
    var id: Int,

    @SerializedName("job")
    var job: String? = null,

    @SerializedName("username")
    var username: String? = null
)