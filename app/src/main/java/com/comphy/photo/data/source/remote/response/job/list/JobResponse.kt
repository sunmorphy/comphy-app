package com.comphy.photo.data.source.remote.response.job.list

import com.google.gson.annotations.SerializedName

data class JobResponse(

    @SerializedName("Status")
	val status: String,

    @SerializedName("data")
	var jobResponseData: JobResponseData? = null,

    @SerializedName("message")
	val message: String
)