package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.remote.response.auth.AuthBody
import com.comphy.photo.data.source.remote.response.auth.AuthResponse
import com.comphy.photo.data.source.remote.response.auth.AuthResponseData
import com.comphy.photo.data.source.remote.response.community.category.CommunityResponse
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.community.created.CreatedCommunityResponse
import com.comphy.photo.data.source.remote.response.job.list.JobResponse
import com.comphy.photo.data.source.remote.response.location.province.ProvinceResponse
import com.comphy.photo.data.source.remote.response.location.regency.RegencyResponse
import com.comphy.photo.data.source.remote.response.post.comment.CommentBody
import com.comphy.photo.data.source.remote.response.post.create.CreatePostBody
import com.comphy.photo.data.source.remote.response.post.create.CreatePostResponse
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponse
import com.comphy.photo.data.source.remote.response.post.update.UpdatePostBody
import com.comphy.photo.data.source.remote.response.upload.UploadResponse
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
import com.comphy.photo.data.source.remote.response.user.detail.UserResponse
import com.comphy.photo.data.source.remote.response.user.location.UserCityResponse
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
    @GET("comphy/user/getCities")
    suspend fun getUserCities(): ApiResponse<UserCityResponse>

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
        "X-Goog-Content-Length-Range: 0,10485760"
    )
    @PUT
    suspend fun uploadImagesPost(
        @Url url: String,
        @Body image: RequestBody
    ): ApiResponse<String>

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
    @GET("comphy/community/list")
    suspend fun getCommunities(): ApiResponse<CommunityResponse>

    @GET("comphy/community/category/list")
    suspend fun getCommunityCategories(): ApiResponse<CommunityResponse>

    @GET("comphy/community/created")
    suspend fun getCreatedCommunities(): ApiResponse<CreatedCommunityResponse>

    @GET("comphy/community/joined")
    suspend fun getJoinedCommunities(): ApiResponse<CreatedCommunityResponse>

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
    suspend fun getDetailCommunity(
        @Query("id") id: Int
    ) // TODO RESPONSE NOT READY YET

    @DELETE("comphy/community/out")
    suspend fun leaveCommunity(
        @Query("communityId") communityId: Int
    ): ApiResponse<CommunityResponse>

    /**
     * Job Vacancies
     */
    @GET("comphy/jobVacancy/list")
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

    @GET("comphy/jobVacancy/details")
    suspend fun getJobDetails(
        @Query("jobVacancyId") jobId: Int
    ) //TODO RESPONSE NOT READY YET

    @GET("comphy/jobVacancy/bookmark/list")
    suspend fun getBookmarkedJobs(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/jobVacancy/bookmark")
    suspend fun bookmarkJob(
        @Query("jobVacancyId") jobId: Int
    ) //TODO RESPONSE NOT READY YET

    @DELETE("comphy/jobVacancy/bookmark/delete")
    suspend fun removeBookmarkedJob(
        @Query("bookmarkJobId") bookmarkJobId: Int
    ) //TODO RESPONSE NOT READY YET

    /**
     * Post
     */
    @GET("comphy/post/feedpost")
    suspend fun getFeedPosts(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): FeedResponse

    @GET("comphy/post/list")
    suspend fun getFeedsByCategory(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("categoryId") categoryId: Int? = null
    ): ApiResponse<FeedResponse>

    @GET("comphy/post/details")
    suspend fun getPostDetails(
        @Query("postId") postId: String
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/post/create")
    suspend fun createPost(
        @Body createPostBody: CreatePostBody
    ): ApiResponse<CreatePostResponse>

    @PUT("comphy/post/update")
    suspend fun updatePost(
        @Body updatePostBody: UpdatePostBody
    ) //TODO RESPONSE NOT READY YET

    @DELETE("comphy/post/delete")
    suspend fun deletePost(
        @Query("postId") postId: String
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/post/like")
    suspend fun likePost(
        @Query("postId") postId: String
    ): ApiResponse<FeedResponse>

    @DELETE("comphy/post/unlike")
    suspend fun unlikePost(
        @Query("postId") postId: String
    ): ApiResponse<FeedResponse>

    @GET("comphy/post/comment/list")
    suspend fun getComments(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("postId") postId: String
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/post/comment/create")
    suspend fun commentPost(
        @Body commentBody: CommentBody
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/post/comment/update")
    suspend fun updateCommentPost(
        @Body commentBody: CommentBody
    ) //TODO RESPONSE NOT READY YET

    @DELETE("comphy/post/comment/delete")
    suspend fun removeCommentPost(
        @Query("commentId") commentId: Int
    ) //TODO RESPONSE NOT READY YET

    /**
     * Event
     */
    @GET("comphy/event/list")
    suspend fun getEvents(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("name") name: String
    ) //TODO RESPONSE NOT READY YET

    @GET("comphy/event")
    suspend fun getEventDetails(
        @Header("eventId") eventId: Int
    ) //TODO RESPONSE NOT READY YET

    @GET("comphy/event/bookmark/user/list")
    suspend fun getBookmarkedEvents(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ) //TODO RESPONSE NOT READY YET

    @POST("comphy/event/bookmark")
    suspend fun bookmarkEvent(
        @Query("eventId") eventId: Int
    ) //TODO RESPONSE NOT READY YET

    @DELETE("comphy/event/bookmark/delete")
    suspend fun removeBookmarkEvent(
        @Query("bookmarkId") bookmarkId: String
    ) //TODO RESPONSE NOT READY YET
}