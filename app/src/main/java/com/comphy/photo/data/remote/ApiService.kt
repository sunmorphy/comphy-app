package com.comphy.photo.data.remote

import com.comphy.photo.data.model.response.auth.AuthBody
import com.comphy.photo.data.model.response.auth.AuthResponse
import com.comphy.photo.data.model.response.auth.Data
import com.comphy.photo.data.model.response.location.province.ProvinceResponse
import com.comphy.photo.data.model.response.location.regency.RegencyResponse
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
     * Register
     */
    @POST("auth/register")
    suspend fun userRegister(
        @Body authBody: AuthBody
    ): ApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun userRegisterGoogle(
        @Body authBody: AuthBody
    ): ApiResponse<AuthResponse>

    @PUT("auth/register/verify-otp")
    suspend fun userRegisterVerify(
        @Query("otp") otp: String,
        @Query("email") email: String
    ): ApiResponse<AuthResponse>

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

    /**
     * Location
     */
    @GET
    suspend fun getProvinces(
        @Url url: String
    ): ApiResponse<ProvinceResponse>

    @GET
    suspend fun getRegencies(
        @Url url: String
    ): ApiResponse<RegencyResponse>

}