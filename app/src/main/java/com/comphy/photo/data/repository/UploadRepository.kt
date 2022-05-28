package com.comphy.photo.data.repository

import com.comphy.photo.data.source.remote.client.ApiService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getUploadLink(
        type: String,
        isPost: Boolean = false,
        amount: Int
    ) = flow {
        val response = apiService.getUploadLink(type, isPost, amount)
        response.suspendOnSuccess {
            emit(data)
        }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun uploadImagesNonPost(
        url: String,
        image: RequestBody,
    ) = flow {
        val response = apiService.uploadImagesNonPost(url, image)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun uploadImagesPost(
        url: String,
        image: RequestBody
    ) = flow {
        val response = apiService.uploadImagesPost(url, image)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun uploadVideosPost(
        url: String,
        video: RequestBody
    ) = flow {
        val response = apiService.uploadVideosPost(url, video)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

}