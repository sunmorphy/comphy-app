package com.comphy.photo.data.source.remote.response.upload

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@SerializedName("Status")
	val status: String,

	@SerializedName("data")
	val data: List<DataItem>,

	@SerializedName("message")
	val message: String
)