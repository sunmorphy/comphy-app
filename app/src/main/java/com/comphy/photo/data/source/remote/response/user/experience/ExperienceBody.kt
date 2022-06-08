package com.comphy.photo.data.source.remote.response.user.experience


import com.google.gson.annotations.SerializedName

data class ExperienceBody(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("company")
    val company: String,

    @SerializedName("position")
    val position: String,

    @SerializedName("years")
    val years: String
)