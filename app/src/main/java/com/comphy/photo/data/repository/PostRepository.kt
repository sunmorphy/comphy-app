package com.comphy.photo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.paging.*
import com.comphy.photo.data.source.remote.response.BaseMessageResponse
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.post.comment.CommentBody
import com.comphy.photo.data.source.remote.response.post.comment.ThirdChildComment
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
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

    fun getFeedPosts() = Pager(
        pagingSourceFactory = { FeedsPagingSource(apiService) },
        config = PagingConfig(pageSize = 10)
    ).flow

    suspend fun getFeeds(
        page: Int? = null
    ) = flow {
        try {
            val response = apiService.getFeeds(page = page)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(FeedResponseContentItem::class.java)
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

    fun getCreatedPosts() = Pager(
        pagingSourceFactory = { CreatedPostsPagingSource(apiService) },
        config = PagingConfig(pageSize = 10)
    ).flow

    fun getFilteredPosts(
        categoryId: Int? = null,
        titlePost: String? = null,
        userId: Int? = null,
        communityId: Int? = null,
        showPhotos: Boolean = false,
        location: String? = null
    ) = Pager(
        pagingSourceFactory = {
            FilteredFeedsPagingSource(
                apiService,
                categoryId,
                titlePost,
                userId,
                communityId,
                showPhotos,
                location
            )
        },
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
                    Timber.e(e)
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getCommentPost(postId: String) = Pager(
        pagingSourceFactory = { CommentPagingSource(apiService, postId) },
        config = PagingConfig(pageSize = 20)
    ).flow

    fun getSecondLevelCommentPost(postId: String, parentId: Int) = Pager(
        pagingSourceFactory = { CommentSecondChildPagingSource(apiService, postId, parentId) },
        config = PagingConfig(pageSize = 20)
    ).flow

    suspend fun getThirdLevelCommentPost(
        postId: String,
        parentId: Int
    ) = flow {
        try {
            val response = apiService.getThirdLevelCommentPost(postId = postId, parentId = parentId)
            response.suspendOnSuccess {
                try {
                    val parsedData = data.data?.parseTo(BaseResponseContent::class.java)
                    val parsedArray =
                        parsedData?.content!!.parseTo(ThirdChildComment::class.java)
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

    suspend fun createPost(postBody: PostBody) = flow {
        val response = apiService.createPost(postBody)
        response.suspendOnSuccess { emit(data) }
            .onError { Timber.tag("On Error").e(message()) }
            .onException { Timber.tag("On Exception").e(message()) }
    }.flowOn(ioDispatcher)

    suspend fun updatePost(
        postBody: PostBody,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.updatePost(postBody)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun deletePost(
        postId: String,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.deletePost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun bookmarkPost(
        postId: String,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.bookmarkPost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun unbookmarkPost(
        savedPostId: String,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.unbookmarkPost(savedPostId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
                onErrorNorException(errorResult?.message)
                Timber.tag("On Error").e(message())
            }
            .onException {
                onErrorNorException(message())
                Timber.tag("On Exception").e(message())
            }
    }.flowOn(ioDispatcher)

    suspend fun likePost(
        postId: String,
        onErrorNorException: (String?) -> Unit
    ) = flow {
        val response = apiService.likePost(postId)
        response.suspendOnSuccess { emit(data) }
            .onError {
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
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
                val errorResult: BaseMessageResponse? =
                    errorBody?.string()?.parseTo(BaseMessageResponse::class.java)
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