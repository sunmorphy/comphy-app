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

class JobRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getJobs(
        page: Int?,
        perPage: Int?
    ) = flow {
        val response = apiService.getJobs(page, perPage)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun getFilteredJobs(
        page: Int?,
        perPage: Int?,
        region: String,
        isFullTime: Boolean = false,
        isPartTime: Boolean = false
    ) = flow {
        val response = apiService.filterJobs(page, perPage, region, isFullTime, isPartTime)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

}