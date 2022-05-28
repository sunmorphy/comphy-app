package com.comphy.photo.data.repository

import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.local.room.location.LocationDao
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseResponse
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.data.source.remote.response.user.following.UserFollowResponseContentItem
import com.comphy.photo.data.source.remote.response.user.location.UserCityResponseData
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

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getUserCities(
        onException: () -> Unit
    ) = flow {
        try {
            val localCities = locationDao.getCities()
            if (localCities.isEmpty()) {
                val response = apiService.getUserCities()
                response.suspendOnSuccess {
                    try {
                        val parsedData = data.data?.parseTo(UserCityResponseData::class.java)
                        parsedData?.cities?.forEach { city ->
                            locationDao.insertCity(CityEntity(city = city))
                        }
                        return@suspendOnSuccess
                    } catch (e: Exception) {
                        println("Cast object fail")
                        Timber.e(e)
                    }
                }
                    .onError { Timber.tag("On Error").e(message()) }
                    .onException {
                        Timber.tag("On Exception").e(message())
                        onException()
                    }
            }
            emit(localCities)
        } catch (e: Exception) {
            println(e)
        }

    }.flowOn(ioDispatcher)

    suspend fun getUserDetails() = flow {
        try {
            val response = apiService.getUserDetails()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(UserResponseData::class.java)
                    emit(parsedData)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    println("Cast object fail")
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            println(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun getUserFollowing() = flow {
        try {
            val response = apiService.getUserFollowing()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(UserFollowResponseContentItem::class.java)
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

    suspend fun getUserFollowers() = flow {
        try {
            val response = apiService.getUserFollowers()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(UserFollowResponseContentItem::class.java)
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

    suspend fun updateUserDetails(
        userDataBody: UserDataBody,
        onError: (errorResponse: BaseResponse) -> Unit,
        onException: (exceptionResponse: String?) -> Unit
    ) = flow {
        val response = apiService.updateUserDetails(userDataBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseResponse =
                    errorBody?.string()!!.parseTo(BaseResponse::class.java)
                onError(responseResult)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

}