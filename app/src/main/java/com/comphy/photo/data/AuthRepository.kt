package com.comphy.photo.data

import com.comphy.photo.data.model.response.google.GoogleBody
import com.comphy.photo.data.model.response.login.LoginBody
import com.comphy.photo.data.remote.ApiService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun userLogin(email: String, password: String) = flow {
        val loginBody = LoginBody(email, password)
        val response = apiService.userLogin(loginBody)
        response.suspendOnSuccess { emit(this.data) }
            .onError { Timber.tag("On Error").e(this.message()) }
            .onException { Timber.tag("On Exception").e(this.message()) }
    }.flowOn(ioDispatcher)

    suspend fun userGoogleLogin(email: String, token: String) = flow {
        val googleBody = GoogleBody(email, token)
        val response = apiService.userGoogleLogin(googleBody)
        response.suspendOnSuccess { emit(this.data) }
            .onError { Timber.tag("On Error").e(this.message()) }
            .onException { Timber.tag("On Exception").e(this.message()) }
    }.flowOn(ioDispatcher)

}