package com.comphy.photo.data.source.remote.response.community.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryCommunityResponseContentItem(

	@SerializedName("deleted_date")
	val deletedDate: String? = null,

	@SerializedName("name")
	val name: String,

	@SerializedName("created_date")
	val createdDate: Long? = null,

	@SerializedName("updated_date")
	val updatedDate: String? = null,

	@SerializedName("id")
	val id: Int
) : Parcelable