package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.remote.response.auth.AuthBody
import com.comphy.photo.data.source.remote.response.auth.AuthResponse
import com.comphy.photo.data.source.remote.response.auth.Data
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.location.province.ProvinceResponse
import com.comphy.photo.data.source.remote.response.location.regency.RegencyResponse
import com.comphy.photo.data.source.remote.response.upload.UploadResponse
import com.comphy.photo.data.source.remote.response.user.UserDataBody
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

    /**
     * User Details
     */
    @GET("comphy/user/details")
    suspend fun getUserDetails(
        @Query("id") userId: Int
    ): ApiResponse<AuthResponse>

    @PUT("comphy/user/update")
    suspend fun updateUserDetails(
        @Body userDataBody: UserDataBody
    ): ApiResponse<AuthResponse>

    /**
     * Media Upload
     */
    @GET("/comphy/file-upload/get-sign")
    suspend fun getUploadLink(
        @Query("type") type: String,
        @Query("isPost") isPost: Boolean,
        @Query("amount") amount: Int
    ): ApiResponse<UploadResponse>

    /**
     * Community
     */
    @POST("comphy/comunity/join")
    suspend fun joinCommunity(
        @Query("communityId") communityId: Int,
        @Query("communityCode") communityCode: String?
    ) // TODO RESPONSE NOT READY YET

    @POST("comphy/comunity/create")
    suspend fun createCommunity(
        @Body createCommunityBody: CreateCommunityBody
    ) // TODO RESPONSE NOT READY YET

    @GET("comphy/comunity/details")
    suspend fun detailCommunity(
        @Query("id") id: Int
    ) // TODO RESPONSE NOT READY YET

    @DELETE("comphy/comunity/out")
    suspend fun leaveCommunity(
        @Query("communityId") communityId: Int
    ) // TODO RESPONSE NOT READY YET
}