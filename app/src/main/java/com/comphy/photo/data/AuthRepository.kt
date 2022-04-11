package com.comphy.photo.data

import com.comphy.photo.data.model.response.auth.AuthBody
import com.comphy.photo.data.model.response.auth.AuthResponse
import com.comphy.photo.data.remote.ApiService
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

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun userLogin(
        email: String,
        password: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit,
    ) = flow {
        val authBody = AuthBody(username = email, password = password)
        val response = apiService.userLogin(authBody)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun userLoginGoogle(
        email: String,
        token: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit,
    ) = flow {
        val authBody = AuthBody(username = email, token = token)
        val response = apiService.userLoginGoogle(authBody)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun userRegister(
        email: String,
        password: String,
        token: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit
    ) = flow {
        val authBody = AuthBody(username = email, password = password, token = token)
        val response = apiService.userRegister(authBody)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun userForgot(
        email: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit
    ) = flow {
        val response = apiService.userForgot(email)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun userForgotVerify(
        otp: String,
        email: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit
    ) = flow {
        val response = apiService.userForgotVerify(otp, email)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun userForgotReset(
        otp: String,
        newPassword: String,
        email: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit
    ) = flow {
        val response = apiService.userForgotReset(otp, newPassword, email)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun userRegisterVerify(
        otp: String,
        email: String,
        responseStatus: (statusCode: Int) -> Unit,
        responseMessage: (message: String) -> Unit
    ) = flow {
        val response = apiService.userRegisterVerify(otp, email)
        response.suspendOnSuccess {
            responseStatus(statusCode.code)
            emit(data)
        }
            .onError {
                val responseResult: AuthResponse =
                    Gson().fromJson(this.errorBody?.string(), AuthResponse::class.java)
                responseStatus(statusCode.code)
                responseMessage(responseResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

}