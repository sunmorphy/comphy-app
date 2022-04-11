package com.comphy.photo.data.model.response.auth

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("created_date")
    val createdDate: Long,
    @SerializedName("deleted_date")
    var deletedDate: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("expiredDateOTP")
    val expiredDateOTP: Any,
    @SerializedName("fullname")
    var fullname: String? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("job")
    var job: String? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("numberPhone")
    var numberPhone: String? = null,
    @SerializedName("otp")
    val otp: Any,
    @SerializedName("otpExpiredDate")
    val otpExpiredDate: Any,
    @SerializedName("password")
    val password: String,
    @SerializedName("roles")
    val roles: List<Role>?,
    @SerializedName("tempPassword")
    val tempPassword: Any,
    @SerializedName("updated_date")
    val updatedDate: Long,
    @SerializedName("username")
    val username: String
)