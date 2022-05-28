package com.comphy.photo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseResponse
import com.comphy.photo.data.source.remote.response.post.comment.CommentBody
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.helpers.FeedsPagingSource
import com.comphy.photo.helpers.FilteredFeedsPagingSource
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

    suspend fun getFilteredFeedPosts(
        categoryId: Int? = null,
        titlePost: String? = null
    ) = Pager(
        pagingSourceFactory = { FilteredFeedsPagingSource(apiService, categoryId, titlePost) },
        config = PagingConfig(10)
    ).flow

    suspend fun getPostDetails(
        postId: String
    ) = flow {
        try {
            val response = apiService.getPostDetails(postId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(FeedResponseContentItem::class.java)
                    emit(parsedData)
                    return@suspendOnSuccess
                } catch (e: Exception) {
                    println("Cast object fail")
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            println(e)
        }
    }

    suspend fun createPost(postBody: PostBody) = flow {
        val response = apiService.createPost(postBody)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun likePost(
        postId: String,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.likePost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseResponse? =
                    errorBody?.string()?.parseTo(BaseResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun unlikePost(
        postId: String,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.unlikePost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseResponse? =
                    errorBody?.string()?.parseTo(BaseResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun commentPost(
        commentBody: CommentBody
    ) = flow {
        val response = apiService.commentPost(commentBody)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun updateCommentPost(
        commentBody: CommentBody
    ) = flow {
        val response = apiService.updateCommentPost(commentBody)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun removeCommentPost(
        commentId: Int
    ) = flow {
        val response = apiService.removeCommentPost(commentId)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

}