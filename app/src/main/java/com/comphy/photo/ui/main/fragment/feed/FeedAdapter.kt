package com.comphy.photo.ui.main.fragment.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContent
import com.comphy.photo.databinding.ItemFeedImageLandscapeBinding
import com.comphy.photo.databinding.ItemFeedTextBinding
import com.comphy.photo.databinding.ItemFeedVideoBinding
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedImageViewHolder
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedTextViewHolder
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedVideoViewHolder
import com.comphy.photo.vo.FeedType
import com.google.android.exoplayer2.ExoPlayer

class FeedAdapter(
    private val player: ExoPlayer?,
    private val onLikePressed: (position: Int, postId: String, isLiked: Boolean) -> Unit
) : PagingDataAdapter<FeedResponseContent, RecyclerView.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FeedType.TEXT -> FeedTextViewHolder(
                ItemFeedTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.IMAGE -> FeedImageViewHolder(
                ItemFeedImageLandscapeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.VIDEO -> FeedVideoViewHolder(
                ItemFeedVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                player
            )
            else -> throw IllegalArgumentException("Missing view type exception ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feed = getItem(position)!!

        when (holder.itemViewType) {
            FeedType.TEXT -> (holder as FeedTextViewHolder).bind(holder.itemView, feed) { postId, isLiked ->
                onLikePressed(position, postId, isLiked)
            }
            FeedType.IMAGE -> (holder as FeedImageViewHolder).bind(holder.itemView, feed) { postId, isLiked ->
                onLikePressed(position, postId, isLiked)
            }
            FeedType.VIDEO -> (holder as FeedVideoViewHolder).bind(holder.itemView, feed) { postId, isLiked ->
                onLikePressed(position, postId, isLiked)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val feed = getItem(position)

        return when {
            feed?.linkPhoto != null -> FeedType.IMAGE
            feed?.linkVideo != null -> FeedType.VIDEO
            else -> FeedType.TEXT
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<FeedResponseContent>() {
            override fun areItemsTheSame(oldItem: FeedResponseContent, newItem: FeedResponseContent): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FeedResponseContent, newItem: FeedResponseContent): Boolean =
                oldItem == newItem
        }
    }
//    override fun onViewRecycled(holder: ViewHolder) {
//        super.onViewRecycled(holder)
//
//        player?.stop()
//    }
//
//    override fun onViewDetachedFromWindow(holder: ViewHolder) {
//        super.onViewDetachedFromWindow(holder)
//
//        player?.release()
//    }
}