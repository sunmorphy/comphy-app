package com.comphy.photo.data.source.remote.response.job.detail


import com.google.gson.annotations.SerializedName

data class JobDetailResponseData(
    @SerializedName("allowance")
    val allowance: String,

    @SerializedName("companyName")
    val companyName: String,

    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: Any? = null,

    @SerializedName("description")
    val description: List<String>,

    @SerializedName("fulltime")
    val fulltime: Boolean,

    @SerializedName("id")
    val id: Int,

    @SerializedName("industry")
    val industry: String,

    @SerializedName("isFulltime")
    val isFulltime: Boolean,

    @SerializedName("jobLevel")
    val jobLevel: String,

    @SerializedName("jobLink")
    val jobLink: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("longApplicationProcess")
    val longApplicationProcess: String,

    @SerializedName("qualification")
    val qualification: String,

    @SerializedName("rangeSalary")
    val rangeSalary: String,

    @SerializedName("requirement")
    val requirement: List<String>,

    @SerializedName("title")
    val title: String,

    @SerializedName("updated_date")
    val updatedDate: String? = null
)