package com.comphy.photo.data.source.remote.response.event

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventResponseContentItem(
    @SerializedName("created_date")
    val createdDate: Long? = null,

    @SerializedName("deleted_date")
    val deletedDate: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("eventName")
    val eventName: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("linkBanner")
    val linkBanner: String? = null,

    @SerializedName("linkEvent")
    val linkEvent: String? = null,

    @SerializedName("location")
    val location: String,

    @SerializedName("presenter")
    val presenter: String? = null,

    @SerializedName("qualification")
    val qualification: String? = null,

    @SerializedName("startDate")
    val startDate: Long? = null,

    @SerializedName("theAudience")
    val theAudience: List<String>? = null,

    @SerializedName("theory")
    val theory: String? = null,

    @SerializedName("time")
    val time: String? = null,

    @SerializedName("typeEvent")
    val typeEvent: Int,

    @SerializedName("updated_date")
    val updatedDate: String? = null
) : Parcelable