package com.comphy.photo.data.source.remote.response.job.bookmark


import com.google.gson.annotations.SerializedName

data class BookmarkJobResponseContentItem(
    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: Any? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("jobVacancy")
    val jobVacancy: JobVacancy,

    @SerializedName("updated_date")
    val updatedDate: String? = null
)