package com.comphy.photo.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.post.comment.CommentResponseContentItem
import com.comphy.photo.utils.JsonParser.parseTo

private const val DEFAULT_PAGE_INDEX = 1

class CommentPagingSource(
    private val apiService: ApiService,
    private val postId: String
) : PagingSource<Int, CommentResponseContentItem>() {
    override fun getRefreshKey(state: PagingState<Int, CommentResponseContentItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentResponseContentItem> {
        val pageIndex = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getCommentPost(
                page = pageIndex,
                perPage = 20,
                postId = postId
            ).data?.parseTo(
                BaseResponseContent::class.java
            )?.content!!.parseTo(
                CommentResponseContentItem::class.java
            )
            LoadResult.Page(
                data = response,
                prevKey = if (pageIndex == DEFAULT_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (response.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}