package com.comphy.photo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.post.create.CreatePostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponse
import com.comphy.photo.helpers.FeedsPagingSource
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

class PostRepository @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

//    suspend fun getFeedPosts(page: Int?, perPage: Int?) = flow {
//        val response = apiService.getFeedPosts(page, perPage)
//        response.suspendOnSuccess { emit(data) }
//            .onError { Timber.tag("On Error").e(message()) }
//            .onException { Timber.tag("On Exception").e(message()) }
//    }.flowOn(ioDispatcher)

    suspend fun getFeedPosts() = Pager(
        pagingSourceFactory = { FeedsPagingSource(apiService) },
        config = PagingConfig(pageSize = 10)
    ).flow

    suspend fun createPost(createPostBody: CreatePostBody) = flow {
        val response = apiService.createPost(createPostBody)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun likePost(
        postId: String,
        onErrorNorException: (String) -> Unit
    ) = flow {
        val response = apiService.likePost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: FeedResponse =
                    Gson().fromJson(errorBody?.string(), FeedResponse::class.java)
                onErrorNorException(errorResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun unlikePost(
        postId: String,
        onErrorNorException: (String) -> Unit
    ) = flow {
        val response = apiService.unlikePost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: FeedResponse =
                    Gson().fromJson(errorBody?.string(), FeedResponse::class.java)
                onErrorNorException(errorResult.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

}