package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.remote.response.auth.AuthBody
import com.comphy.photo.data.source.remote.response.auth.AuthResponse
import com.comphy.photo.data.source.remote.response.auth.AuthResponseData
import com.comphy.photo.data.source.remote.response.community.category.CommunityResponse
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.job.list.JobResponse
import com.comphy.photo.data.source.remote.response.location.province.ProvinceResponse
import com.comphy.photo.data.source.remote.response.location.regency.RegencyResponse
import com.comphy.photo.data.source.remote.response.post.create.CreatePostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponse
import com.comphy.photo.data.source.remote.response.post.update.UpdatePostBody
import com.comphy.photo.data.source.remote.response.upload.UploadResponse
import com.comphy.photo.data.source.remote.response.user.UserDataBody
import com.comphy.photo.data.source.remote.response.user.UserResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    /**
     * Login
     */
    @POST("auth/login")
    suspend fun userLogin(
        @Body authBody: AuthBody
    ): ApiResponse<AuthResponseData>

    @POST("auth/login")
    suspend fun userLoginGoogle(
        @Body authBody: AuthBody
    ): ApiResponse<AuthResponseData>

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
    suspend fun getUserDetails(): ApiResponse<UserResponse>

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

    @Headers(
        "Content-Type: image/*",
        "X-Goog-Content-Length-Range: 0,3145728"
    )
    @PUT
    suspend fun uploadImagesNonPost(
        @Url url: String,
        @Body image: RequestBody
    ): ApiResponse<String>

    @Headers(
        "Content-Type: image/*",
        "X-Goog-Content-Length-Range: 0,5242880"
    )
    @PUT
    suspend fun uploadImagesPost(
        @Url url: String,
        @Body image: RequestBody
    ): ApiResponse<Any>

    @Headers(
        "Content-Type: video/*",
        "X-Goog-Content-Length-Range: 0,104857600"
    )
    @PUT
    suspend fun uploadVideosPost(
        @Url url: String,
        @Body video: RequestBody
    ): ApiResponse<Any>

    /**
     * Community
     */
    @POST("comphy/community/join")
    suspend fun joinCommunity(
        @Query("communityId") communityId: Int,
        @Query("communityCode") communityCode: String? = null
    ) // TODO RESPONSE NOT READY YET

    @POST("comphy/community/create")
    suspend fun createCommunity(
        @Body createCommunityBody: CreateCommunityBody
    ): ApiResponse<CommunityResponse>

    @GET("comphy/community/details")
    suspend fun detailCommunity(
        @Query("id") id: Int
    ) // TODO RESPONSE NOT READY YET

    @DELETE("comphy/community/out")
    suspend fun leaveCommunity(
        @Query("communityId") communityId: Int
    ) // TODO RESPONSE NOT READY YET

    @GET("comphy/community/list")
    suspend fun getCommunities(): ApiResponse<CommunityResponse>

    @GET("comphy/community/category/list")
    suspend fun getCommunityCategories(): ApiResponse<CommunityResponse>

    /**
     * Job Vacancies
     */
    @GET("comphy/jobVacancy/listPage")
    suspend fun getJobs(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<JobResponse>

    @GET("comphy/jobVacancy/filter")
    suspend fun filterJobs(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("region") region: String,
        @Query("fullTime") fullTime: Boolean = false,
        @Query("partTime") partTime: Boolean = false,
    ): ApiResponse<JobResponse>

    /**
     * Post
     */
    @GET("comphy/post/feedpost")
    suspend fun getFeedPosts(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<FeedResponse>

    @GET("comphy/post/details")
    suspend fun getPostDetails(
        @Query("postId") postId: String
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/post/create")
    suspend fun createPost(
        @Body createPostBody: CreatePostBody
    ) //TODO RESPONSE NOT READY YET

    @PUT("comphy/post/update")
    suspend fun updatePost(
        @Body updatePostBody: UpdatePostBody
    ) //TODO RESPONSE NOT READY YET

    @DELETE("comphy/post/delete")
    suspend fun deletePost(
        @Query("postId") postId: String
    ) //TODO RESPONSE NOT READY YET
}