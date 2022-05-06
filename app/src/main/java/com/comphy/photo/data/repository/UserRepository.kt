package com.comphy.photo.data.repository

import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.auth.AuthResponse
import com.comphy.photo.data.source.remote.response.user.UserDataBody
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
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getUserDetails(userId: Int) = flow {
        val response = apiService.getUserDetails(userId)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
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