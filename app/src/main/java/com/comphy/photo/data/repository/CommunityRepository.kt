package com.comphy.photo.data.repository

import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseMessageResponse
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.community.edit.EditCommunityBody
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.member.MemberCommunityResponseContentItem
import com.comphy.photo.utils.JsonParser.parseTo
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getFilteredCommunities(
        communityName: String? = null,
        categoryId: Int? = null,
        location: String? = null,
        onErrorNorException: (exceptionResponse: String?) -> Unit
    ) = flow {
        try {
            val response = apiService.getFilteredCommunities(communityName, categoryId, location)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(FollowCommunityResponseContentItem::class.java)
                    emit(parsedArray)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError {
                    val responseResult: BaseMessageResponse? =
                        errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                    onErrorNorException(responseResult?.message)
                    Timber.tag("On Error").e(message())
                }
                .onException {
                    onErrorNorException(message)
                    Timber.tag("On Exception").e(message())
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun getCommunityMembers(
        communityId: Int,
        onErrorNorException: (exceptionResponse: String?) -> Unit
    ) = flow {
        try {
            val response = apiService.getCommunityMembers(communityId = communityId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(MemberCommunityResponseContentItem::class.java)
                    emit(parsedArray)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError {
                    val responseResult: BaseMessageResponse? =
                        errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                    onErrorNorException(responseResult?.message)
                    Timber.tag("On Error").e(message())
                }
                .onException {
                    onErrorNorException(message)
                    Timber.tag("On Exception").e(message())
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun getAdminCommunityDetails(communityId: Int) = flow {
        try {
            val response = apiService.getAdminDetailCommunity(communityId)
            response.suspendOnSuccess {
                try {
                    val parsedData =
                        data.data?.parseTo(FollowCommunityResponseContentItem::class.java)
                    emit(parsedData)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            println(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun editPrivateCommunityCode(
        communityId: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.editPrivateCommunityCode(communityId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                if (responseResult != null) onErrorNorException(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun banUserCommunity(
        userId: Int,
        communityId: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.banUserCommunity(userId, communityId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                if (responseResult != null) onErrorNorException(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun getCommunityDetails(communityId: Int) = flow {
        try {
            val response = apiService.getDetailCommunity(communityId)
            response.suspendOnSuccess {
                try {
                    val parsedData =
                        data.data?.parseTo(FollowCommunityResponseContentItem::class.java)
                    emit(parsedData)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            println(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun createCommunity(
        createCommunityBody: CreateCommunityBody,
        onError: (errorResponse: BaseMessageResponse) -> Unit,
        onException: (exceptionResponse: String?) -> Unit
    ) = flow {
        val response = apiService.createCommunity(createCommunityBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                if (responseResult != null) onError(responseResult)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun editCommunity(
        editCommunityBody: EditCommunityBody,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.editCommunityDetail(editCommunityBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                if (responseResult != null) onErrorNorException(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun joinCommunity(
        communityId: Int,
        communityCode: String?,
        onErrorNorException: (exceptionResponse: String?) -> Unit
    ) = flow {
        val response = apiService.joinCommunity(communityId, communityCode)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(responseResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun leaveCommunity(
        communityId: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.leaveCommunity(communityId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                if (responseResult != null) onErrorNorException(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun deleteCommunity(
        communityId: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.deleteCommunity(communityId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                if (responseResult != null) onErrorNorException(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun getCommunityCategories() = flow {
        try {
            val response = apiService.getCommunityCategories()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(CategoryCommunityResponseContentItem::class.java)
                    emit(parsedArray)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun getCreatedCommunities(
        onException: (String?) -> Unit
    ) = flow {
        try {
            val response = apiService.getCreatedCommunities()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(FollowCommunityResponseContentItem::class.java)
                    emit(parsedArray)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException {
                    onException(message)
                    Timber.tag("On Exception").e(message())
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun getJoinedCommunities(
        onException: () -> Unit
    ) = flow {
        try {
            val response = apiService.getJoinedCommunities()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(FollowCommunityResponseContentItem::class.java)
                    emit(parsedArray)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException {
                    onException()
                    Timber.tag("On Exception").e(message())
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }.flowOn(ioDispatcher)
}