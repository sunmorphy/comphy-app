package com.comphy.photo.data

import com.comphy.photo.data.model.response.auth.AuthResponse
import com.comphy.photo.data.model.response.biodata.BiodataBody
import com.comphy.photo.data.model.response.biodata.BiodataResponse
import com.comphy.photo.data.remote.ApiService
import com.google.gson.Gson
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class BiodataRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun updateUserData(
        fullName: String,
        username: String,
        location: String,
        numberPhone: String? = null,
        job: String,
        description: String,
        socialMedia: String,
        id: Int,
        onError: (errorResponse: BiodataResponse) -> Unit,
        onException: (exceptionResponse: String?) -> Unit
    ) = flow {
        val body = BiodataBody (fullName,username, location,numberPhone, job, description, socialMedia, id)
        val biodataResponse = apiService.updateUserData(body)
        biodataResponse.suspendOnSuccess { emit(data)}
            .onError {
                val responseResult: BiodataResponse =
                    Gson().fromJson(this.errorBody?.string(), BiodataResponse::class.java)
                onError (responseResult)
                Timber.tag("On Error").e(message()) }
            .onException{
                onException(message)
                Timber.tag("On Exception").e(message()) }

    }.flowOn(ioDispatcher)
}