package com.comphy.photo.data.model.response.community.create

import com.google.gson.annotations.SerializedName

data class CreateCommunityBody(

    @SerializedName("communityType")
    val communityType: String? = null,

    @SerializedName("categoryCommunityId")
    val categoryCommunityId: Int? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("communityName")
    val communityName: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("Id")
    val id: Int? = null
)
