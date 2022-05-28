package com.comphy.photo.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.utils.JsonParser.parseTo

private const val DEFAULT_PAGE_INDEX = 1

class FilteredFeedsPagingSource(
    private val apiService: ApiService,
    private var categoryId: Int? = null,
    private var titlePost: String? = null
) : PagingSource<Int, FeedResponseContentItem>() {
    override fun getRefreshKey(state: PagingState<Int, FeedResponseContentItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedResponseContentItem> {
        val pageIndex = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getFilteredFeedPosts(
                page = pageIndex,
                categoryId = categoryId,
                titlePost = titlePost
            )
            val parsedData = response.data?.parseTo(BaseResponseContent::class.java)
            val parsedArray =
                parsedData?.content!!.parseTo(FeedResponseContentItem::class.java)
            LoadResult.Page(
                data = parsedArray,
                prevKey = if (pageIndex == DEFAULT_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (parsedArray.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}