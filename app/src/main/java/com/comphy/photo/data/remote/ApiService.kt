package com.comphy.photo.data.remote

import com.comphy.photo.data.model.response.auth.AuthBody
import com.comphy.photo.data.model.response.auth.AuthResponse
import com.comphy.photo.data.model.response.auth.Data
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.*

interface ApiService {

    /**
     * Login
     */
    @POST("auth/login")
    suspend fun userLogin(
        @Body authBody: AuthBody
    ): ApiResponse<Data>

    @POST("auth/login")
    suspend fun userLoginGoogle(
        @Body authBody: AuthBody
    ): ApiResponse<Data>

    /**
     * Forgot Password
     */
    @POST("auth/forgot-password")
    suspend fun userForgot(
        @Query("email") email: String
    ): ApiResponse<AuthResponse>

    @POST("auth/forgot-password/verify")
    suspend fun userForgotVerify(
        @Query("otp") otp: String,
        @Query("email") email: String
    ): ApiResponse<AuthResponse>

    @POST("auth/forgot-password/reset")
    suspend fun userForgotReset(
        @Query("otp") otp: String,
        @Query("newPassword") newPassword: String,
        @Query("email") email: String
    ): ApiResponse<AuthResponse>

    /**
     * Refresh Token
     */
    @POST("auth/refresh/token")
    suspend fun userRefresh(
        @Header("refresh-token") refreshToken: String
    ): ApiResponse<AuthResponse>
}