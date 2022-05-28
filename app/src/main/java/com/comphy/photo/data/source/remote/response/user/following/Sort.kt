package com.comphy.photo.data.source.remote.response.user.following


import com.google.gson.annotations.SerializedName

data class Sort(
    @SerializedName("empty")
    val empty: Boolean = false,

    @SerializedName("sorted")
    val sorted: Boolean = false,

    @SerializedName("unsorted")
    val unsorted: Boolean = false
)