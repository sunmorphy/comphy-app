package com.comphy.photo.data.repository

import com.comphy.photo.data.source.remote.client.ApiService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getFeedPosts(page: Int?, perPage: Int?) = flow {
        val response = apiService.getFeedPosts(page, perPage)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

}