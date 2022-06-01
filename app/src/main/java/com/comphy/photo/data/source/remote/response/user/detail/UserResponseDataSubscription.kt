package com.comphy.photo.data.source.remote.response.user.detail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponseDataSubscription(

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("limitCreateCommunity")
	val limitCreateCommunity: Int? = null,

	@SerializedName("limitJoinCommunity")
	val limitJoinCommunity: Int? = null
) : Parcelable