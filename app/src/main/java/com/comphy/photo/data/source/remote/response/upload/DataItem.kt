package com.comphy.photo.data.source.remote.response.upload

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("storagePath")
	val storagePath: String? = null,

	@field:SerializedName("storageUrl")
	val storageUrl: String? = null
)