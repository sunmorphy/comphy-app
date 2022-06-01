package com.comphy.photo.data.source.remote.response.user.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    var experiences: List<String?>? = null,

    @SerializedName("deleted_date")
    var deletedDate: String? = null,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("created_date")
    var createdDate: Long? = null,

    @SerializedName("updated_date")
    var updatedDate: String? = null,

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
) : Parcelable