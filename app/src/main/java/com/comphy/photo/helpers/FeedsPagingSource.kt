package com.comphy.photo.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContent

private const val DEFAULT_PAGE_INDEX = 1

class FeedsPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, FeedResponseContent>() {
    override fun getRefreshKey(state: PagingState<Int, FeedResponseContent>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedResponseContent> {
        val pageIndex = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getFeedPosts(pageIndex).data!!.content
            LoadResult.Page(
                data = response,
                prevKey = if (pageIndex == DEFAULT_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (response.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
//        response.suspendOnSuccess {
//            val feeds = data.data!!.content
//            LoadResult.Page(
//                data = feeds,
//                prevKey = if (pageIndex == DEFAULT_PAGE_INDEX) null else pageIndex - 1,
//                nextKey = if (feeds.isEmpty()) null else pageIndex + 1
//            )
//        }.onException { LoadResult.Error(exception.) }
    }
}