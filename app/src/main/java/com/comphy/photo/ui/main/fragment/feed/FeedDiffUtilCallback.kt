package com.comphy.photo.ui.main.fragment.feed

import androidx.recyclerview.widget.DiffUtil
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem

class FeedDiffUtilCallback(
    private val oldList: List<FeedResponseContentItem>,
    private val newList: List<FeedResponseContentItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id
}