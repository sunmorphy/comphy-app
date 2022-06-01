package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.remote.response.BaseMessageResponse
import com.comphy.photo.data.source.remote.response.BaseResponse
import com.comphy.photo.data.source.remote.response.auth.AuthBody
import com.comphy.photo.data.source.remote.response.auth.AuthResponseData
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.community.edit.EditCommunityBody
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
    suspend fun userRefresh(
        @Header("refresh-token") refreshToken: String
    ): ApiResponse<BaseResponse>

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

    @POST("comphy/user/follow")
    suspend fun followUser(
        @Query("userIdFollowed") userIdFollowed: Int
    ): ApiResponse<BaseMessageResponse>

    @DELETE("comphy/user/unfollow")
    suspend fun unfollowUser(
        @Query("userIdFollowed") userIdFollowed: Int
    ): ApiResponse<BaseMessageResponse>

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
    suspend fun getFilteredCommunities(
        @Query("communityName") communityName: String? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("location") location: String? = null
    ): ApiResponse<BaseResponse>

    @GET("comphy/community/member/list")
    suspend fun getCommunityMembers(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("communityId") communityId : Int
    ): ApiResponse<BaseResponse>

    @GET("comphy/community/category/list")
    suspend fun getCommunityCategories(): ApiResponse<BaseResponse>

    @GET("comphy/community/created")
    suspend fun getCreatedCommunities(): ApiResponse<BaseResponse>

    @GET("comphy/community/joined")
    suspend fun getJoinedCommunities(): ApiResponse<BaseResponse>

    @GET("comphy/community/details")
    suspend fun getDetailCommunity(
        @Query("id") id: Int
    ): ApiResponse<BaseResponse>

    @POST("comphy/community/join")
    suspend fun joinCommunity(
        @Query("communityId") communityId: Int,
        @Query("communityCode") communityCode: String? = null
    ): ApiResponse<BaseResponse>

    @POST("comphy/community/create")
    suspend fun createCommunity(
        @Body createCommunityBody: CreateCommunityBody
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
        @Body editCommunityBody: EditCommunityBody
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
    suspend fun getFilteredPosts(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("titlePost") titlePost: String? = null,
        @Query("userId") userId: Int? = null,
        @Query("communityId") communityId: Int? = null,
        @Query("showPhotos") showPhotos: Boolean = false,
        @Query("location") location: String? = null
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

    @POST("comphy/post/saved")
    suspend fun bookmarkPost(
        @Query("postId") postId: String
    ): ApiResponse<BaseMessageResponse>

    @DELETE("comphy/post/saved/delete")
    suspend fun unbookmarkPost(
        @Query("savedPostId") savedPostId: String
    ): ApiResponse<BaseMessageResponse>

    @POST("comphy/post/like")
    suspend fun likePost(
        @Query("postId") postId: String
    ): ApiResponse<BaseMessageResponse>

    @DELETE("comphy/post/unlike")
    suspend fun unlikePost(
        @Query("postId") postId: String
    ): ApiResponse<BaseMessageResponse>

    @GET("comphy/post/comments/list")
    suspend fun getCommentPost(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("postId") postId: String,
        @Query("parentId") parentId: Int? = null
    ): BaseResponse

    @GET("comphy/post/comments/list")
    suspend fun getSecondLevelCommentPost(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("postId") postId: String,
        @Query("parentId") parentId: Int
    ): BaseResponse

    @GET("comphy/post/comments/list")
    suspend fun getThirdLevelCommentPost(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("postId") postId: String,
        @Query("parentId") parentId: Int
    ): ApiResponse<BaseResponse>

    @POST("comphy/post/comments/create")
    suspend fun commentPost(
        @Body commentBody: CommentBody
    ): ApiResponse<BaseMessageResponse>

    @POST("comphy/post/comments/update")
    suspend fun updateCommentPost(
        @Body commentBody: CommentBody
    ): ApiResponse<BaseMessageResponse>

    @DELETE("comphy/post/comments/delete")
    suspend fun removeCommentPost(
        @Query("commentId") commentId: Int
    ): ApiResponse<BaseMessageResponse>

    /**
     * Event
     */
    @GET("comphy/event/list")
    suspend fun getEvents(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("name") name: String? = null
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