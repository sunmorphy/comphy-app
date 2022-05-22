package com.comphy.photo.data.source.remote.response.post.feed


import com.google.gson.annotations.SerializedName

data class FeedResponseSort(
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)