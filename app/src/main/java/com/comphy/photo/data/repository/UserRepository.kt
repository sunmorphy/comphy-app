package com.comphy.photo.data.repository

import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.local.room.location.LocationDao
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseMessageResponse
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.data.source.remote.response.user.experience.ExperienceBody
import com.comphy.photo.data.source.remote.response.user.follower.UserFollowersResponseContentItem
import com.comphy.photo.data.source.remote.response.user.following.UserFollowingResponseContentItem
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
            if (locationDao.getCities().isEmpty()) {
                val response = apiService.getUserCities()
                response.suspendOnSuccess {
                    try {
                        val parsedData = data.data?.parseTo(UserCityResponseData::class.java)
                        parsedData!!.cities.forEach { city ->
                            locationDao.insertCity(CityEntity(city = city))
                        }
                        emit(locationDao.getCities())
                        return@suspendOnSuccess
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
                    .onError { Timber.tag("On Error").e(message()) }
                    .onException {
                        Timber.tag("On Exception").e(message())
                        onException()
                    }
            }
            emit(locationDao.getCities())
        } catch (e: Exception) {
            println(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun getFilteredUsers(
        name: String?,
        location: String?
    ) = flow {
        try {
            val response = apiService.getFilteredUsers(name = name, location = location)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(UserResponseData::class.java)
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

    suspend fun getUserDetails() = flow {
        try {
            val response = apiService.getUserDetails()
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(UserResponseData::class.java)
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

    suspend fun getUserDetailsById(userId: Int) = flow {
        try {
            val response = apiService.getUserDetails(userId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(UserResponseData::class.java)
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

    suspend fun getUserFollowing(userId: Int?) = flow {
        try {
            val response = apiService.getUserFollowing(userId = userId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(UserFollowingResponseContentItem::class.java)
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

    suspend fun getUserFollowers(userId: Int?) = flow {
        try {
            val response = apiService.getUserFollowers(userId = userId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(UserFollowersResponseContentItem::class.java)
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
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.updateUserDetails(userDataBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: BaseMessageResponse =
                    errorBody?.string()!!.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun followUser(
        userIdFollowed: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.followUser(userIdFollowed)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun unfollowUser(
        userIdFollowed: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.unfollowUser(userIdFollowed)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun createExperience(
        experienceBody: ExperienceBody,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.createExperience(experienceBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun updateExperience(
        experienceBody: ExperienceBody,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.updateExperience(experienceBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun deleteExperience(
        experienceId: Int,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.deleteExperience(experienceId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun verifyPassword(
        password: String
    ) = flow {
        val response = apiService.verifyPassword(password)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun changePassword(
        newPassword: String,
        onError: (String?) -> Unit
    ) = flow {
        val response = apiService.changePassword(newPassword)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onError(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

}