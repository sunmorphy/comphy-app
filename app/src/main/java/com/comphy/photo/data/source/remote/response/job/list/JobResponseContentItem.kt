package com.comphy.photo.data.source.remote.response.job.list


import com.google.gson.annotations.SerializedName

data class JobResponseContentItem(
    @SerializedName("companyName")
    val companyName: String,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: Any? = null,

    @SerializedName("fulltime")
    val fulltime: Boolean,

    @SerializedName("id")
    val id: Int,

    @SerializedName("isFulltime")
    val isFulltime: Boolean,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("rangeSalary")
    val rangeSalary: String? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("updated_date")
    val updatedDate: String? = null
)