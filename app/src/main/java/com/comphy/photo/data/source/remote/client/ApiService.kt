package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.remote.response.BaseResponse
import com.comphy.photo.data.source.remote.response.auth.AuthBody
import com.comphy.photo.data.source.remote.response.auth.AuthResponseData
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.job.list.JobResponse
import com.comphy.photo.data.source.remote.response.post.comment.CommentBody
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.upload.UploadResponse
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
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
    ): ApiResponse<BaseResponse>

    @POST("auth/register")
    suspend fun userRegisterGoogle(
        @Body authBody: AuthBody
    ): ApiResponse<BaseResponse>

    @PUT("auth/register/verify-otp")
    suspend fun userRegisterVerify(
        @Query("otp") otp: String,
        @Query("email") email: String
    ): ApiResponse<BaseResponse>

    /**
     * Forgot Password
     */
    @POST("auth/forgot-password")
    suspend fun userForgot(
        @Query("email") email: String
    ): ApiResponse<BaseResponse>

    @POST("auth/forgot-password/verify")
    suspend fun userForgotVerify(
        @Query("otp") otp: String,
        @Query("email") email: String
    ): ApiResponse<BaseResponse>

    @POST("auth/forgot-password/reset")
    suspend fun userForgotReset(
        @Query("otp") otp: String,
        @Query("newPassword") newPassword: String,
        @Query("email") email: String
    ): ApiResponse<BaseResponse>

    /**
     * Refresh Token
     */
    @POST("auth/refresh/token")
    fun userRefresh(
        @Header("refresh-token") refreshToken: String
    ): BaseResponse

    /**
     * User
     */
    @GET("comphy/user/getCities")
    suspend fun getUserCities(): ApiResponse<BaseResponse>

    @GET("comphy/user/details")
    suspend fun getUserDetails(): ApiResponse<BaseResponse>

    @GET("comphy/user/getFollowing")
    suspend fun getUserFollowing(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<BaseResponse>

    @GET("comphy/user/getFollowers")
    suspend fun getUserFollowers(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<BaseResponse>

    @PUT("comphy/user/update")
    suspend fun updateUserDetails(
        @Body userDataBody: UserDataBody
    ): ApiResponse<BaseResponse>

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
    suspend fun getCommunities(
        @Query("communityName") communityName: String? = null
    ): ApiResponse<BaseResponse>

    @GET("comphy/community/category/list")
    suspend fun getCommunityCategories(): ApiResponse<BaseResponse>

    @GET("comphy/community/created")
    suspend fun getCreatedCommunities(): ApiResponse<BaseResponse>

    @GET("comphy/community/joined")
    suspend fun getJoinedCommunities(): ApiResponse<BaseResponse>

    @POST("comphy/community/join")
    suspend fun joinCommunity(
        @Query("communityId") communityId: Int,
        @Query("communityCode") communityCode: String? = null
    ): ApiResponse<BaseResponse>

    @POST("comphy/community/create")
    suspend fun createCommunity(
        @Body createCommunityBody: CreateCommunityBody
    ): ApiResponse<BaseResponse>

    @GET("comphy/community/details")
    suspend fun getDetailCommunity(
        @Query("id") id: Int
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/community/out")
    suspend fun leaveCommunity(
        @Query("communityId") communityId: Int
    ): ApiResponse<BaseResponse>

    /**
     * Community Admin
     */
    @GET("comphy/community/admin/details")
    suspend fun getAdminDetailCommunity(
        @Query("communityId") communityId: Int
    ): ApiResponse<BaseResponse>

    @PUT("comphy/community/admin/edit")
    suspend fun editCommunityDetail(
        @Body editCommunityBody: CreateCommunityBody
    ): ApiResponse<BaseResponse>

    @PUT("comphy/community/admin/communitycode")
    suspend fun editPrivateCommunityCode(
        @Query("communityId") communityId: Int
    ): ApiResponse<BaseResponse>

    @PUT("comphy/community/admin/ban-user")
    suspend fun banUserCommunity(
        @Query("userId") userId: Int,
        @Query("communityId") communityId: Int
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/community/admin/delete")
    suspend fun deleteCommunity(
        @Query("communityId") communityId: Int
    ): ApiResponse<BaseResponse>

    /**
     * Job Vacancies
     */
    @GET("comphy/jobVacancy/list")
    suspend fun getJobs(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<BaseResponse>

    @GET("comphy/jobVacancy/filter") // TODO FILTER NOT READY YET SIGH
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
    ): ApiResponse<BaseResponse>

    @GET("comphy/jobVacancy/bookmark/list")
    suspend fun getBookmarkedJobs(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<BaseResponse>

    @POST("comphy/jobVacancy/bookmark")
    suspend fun bookmarkJob(
        @Query("jobVacancyId") jobId: Int
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/jobVacancy/bookmark/delete")
    suspend fun unBookmarkJob(
        @Query("bookmarkJobId") bookmarkedJobId: Int
    ): ApiResponse<BaseResponse>

    /**
     * Post
     */
    @GET("comphy/post/feedpost")
    suspend fun getFeedPosts(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): BaseResponse

    @GET("comphy/post/list")
    suspend fun getFilteredFeedPosts(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("titlePost") titlePost: String? = null
    ): BaseResponse

    @GET("comphy/post/details")
    suspend fun getPostDetails(
        @Query("postId") postId: String
    ): ApiResponse<BaseResponse>

    @POST("comphy/post/create")
    suspend fun createPost(
        @Body postBody: PostBody
    ): ApiResponse<BaseResponse>

    @PUT("comphy/post/update")
    suspend fun updatePost(
        @Body postBody: PostBody
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/post/delete")
    suspend fun deletePost(
        @Query("postId") postId: String
    ): ApiResponse<BaseResponse>

    @POST("comphy/post/like")
    suspend fun likePost(
        @Query("postId") postId: String
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/post/unlike")
    suspend fun unlikePost(
        @Query("postId") postId: String
    ): ApiResponse<BaseResponse>

    @GET("comphy/post/comment/list")
    suspend fun getComments(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("postId") postId: String
    ): ApiResponse<BaseResponse>

    @POST("comphy/post/comment/create")
    suspend fun commentPost(
        @Body commentBody: CommentBody
    ): ApiResponse<BaseResponse>

    @POST("comphy/post/comment/update")
    suspend fun updateCommentPost(
        @Body commentBody: CommentBody
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/post/comment/delete")
    suspend fun removeCommentPost(
        @Query("commentId") commentId: Int
    ): ApiResponse<BaseResponse>

    /**
     * Event
     */
    @GET("comphy/event/list")
    suspend fun getEvents(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("name") name: String
    ): ApiResponse<BaseResponse>

    @GET("comphy/event")
    suspend fun getEventDetails(
        @Header("eventId") eventId: Int
    ): ApiResponse<BaseResponse>

    @GET("comphy/event/bookmark/user/list")
    suspend fun getBookmarkedEvents(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null
    ): ApiResponse<BaseResponse>

    @POST("comphy/event/bookmark")
    suspend fun bookmarkEvent(
        @Query("eventId") eventId: Int
    ): ApiResponse<BaseResponse>

    @DELETE("comphy/event/bookmark/delete")
    suspend fun removeBookmarkEvent(
        @Query("bookmarkId") bookmarkId: String
    ): ApiResponse<BaseResponse>
}