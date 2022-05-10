package com.comphy.photo.data.source.remote.response.upload

import com.google.gson.annotations.SerializedName

data class DataItem(

	@SerializedName("storagePath")
	val storagePath: String,

	@SerializedName("storageUrl")
	val storageUrl: String
)