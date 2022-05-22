package com.comphy.photo.data.model

data class FeedModel(
    val feedType: Int,
    val caption: String,
    val description: String,
    val url: String? = null,
    val mediaOrientation: Int? = null
)