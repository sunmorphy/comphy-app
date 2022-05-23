package com.comphy.photo.data.repository

import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.local.room.location.LocationDao
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.auth.AuthResponse
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.google.gson.Gson
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
        val localCities = locationDao.getCities()
        if (localCities.isEmpty()) {
            val response = apiService.getUserCities()
            response.suspendOnSuccess {
                data.userCityResponseData.cities.forEach { city ->
                    locationDao.insertCity(CityEntity(city = city))
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException {
                    Timber.tag("On Exception").e(message())
                    onException()
                }
        }
        emit(localCities)

    }.flowOn(ioDispatcher)

    suspend fun getUserDetails() = flow {
        try {
            val response = apiService.getUserDetails()
            response.suspendOnSuccess {
                val temp = data.data as UserResponseData?
                if (temp != null) emit(temp) else emit(null)
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            println(e)
        }
    }.flowOn(ioDispatcher)

    suspend fun updateUserDetails(
        userDataBody: UserDataBody,
        onError: (errorResponse: AuthResponse) -> Unit,
        onException: (exceptionResponse: String?) -> Unit
    ) = flow {
        val response = apiService.updateUserDetails(userDataBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                onError(responseResult)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onException(message)
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

}