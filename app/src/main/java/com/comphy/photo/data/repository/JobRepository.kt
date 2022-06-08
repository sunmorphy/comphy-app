package com.comphy.photo.data.repository

import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.job.bookmark.BookmarkJobResponseContentItem
import com.comphy.photo.data.source.remote.response.job.detail.JobDetailResponseData
import com.comphy.photo.data.source.remote.response.job.list.JobResponseContentItem
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

class JobRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getJobs(
        page: Int?,
        perPage: Int?
    ) = flow {
        try {
            val response = apiService.getJobs(page, perPage)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(JobResponseContentItem::class.java)
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

    suspend fun getFilteredJobs(
        page: Int?,
        perPage: Int?,
        title: String? = null,
        location: String? = null
    ) = flow {
        try {
            val response = apiService.getFilteredJobs(page, perPage, title, location)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(JobResponseContentItem::class.java)
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

    suspend fun getJobDetails(
        jobId: Int
    ) = flow {
        try {
            val response = apiService.getJobDetails(jobId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(JobDetailResponseData::class.java)
                    emit(parsedData)
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
    }

    suspend fun getBookmarkedJobs(
        page: Int? = null,
        perPage: Int? = null
    ) = flow {
        try {
            val response = apiService.getBookmarkedJobs(page, perPage)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(BookmarkJobResponseContentItem::class.java)
                    emit(parsedArray)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException {
                    Timber.tag("On Exception").e(message())
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun bookmarkJob(
        jobId: Int
    ) = flow {
        val response = apiService.bookmarkJob(jobId)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun unBookmarkJob(
        bookmarkedJobId: Int
    ) = flow {
        val response = apiService.unBookmarkJob(bookmarkedJobId)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

}