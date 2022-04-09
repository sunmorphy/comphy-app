package com.comphy.photo.data.remote

import com.comphy.photo.data.model.response.google.GoogleBody
import com.comphy.photo.data.model.response.login.LoginBody
import com.comphy.photo.data.model.response.login.LoginResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun userLogin(@Body loginBody: LoginBody): ApiResponse<LoginResponse>

    @POST("auth/login")
    suspend fun userGoogleLogin(@Body googleBody: GoogleBody): ApiResponse<LoginResponse>

}