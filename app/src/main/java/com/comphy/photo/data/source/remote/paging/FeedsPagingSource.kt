package com.comphy.photo.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.comphy.photo.data.source.remote.client.ApiService
import com.comphy.photo.data.source.remote.response.BaseResponseContent
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.utils.Constants.DEFAULT_PAGE_INDEX
import com.comphy.photo.utils.JsonParser.parseTo
import okio.IOException
import retrofit2.HttpException

class FeedsPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, FeedResponseContentItem>() {
    override fun getRefreshKey(state: PagingState<Int, FeedResponseContentItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedResponseContentItem> {
        val pageIndex = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getFeedPosts(
                page = pageIndex,
                perPage = 20
            ).data?.parseTo(
                BaseResponseContent::class.java
            )?.content!!.parseTo(
                FeedResponseContentItem::class.java
            )
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else pageIndex + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}