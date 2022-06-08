package com.comphy.photo.ui.main.fragment.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.ItemPostImageBinding
import com.comphy.photo.databinding.ItemPostTextBinding
import com.comphy.photo.databinding.ItemPostVideoBinding
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedImageViewHolder
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedTextViewHolder
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedVideoViewHolder
import com.comphy.photo.vo.FeedType

class FeedsAdapter(
    private val userData: UserResponseData,
    private val onProfileClick: (userId: Int) -> Unit,
    private val onFollowClick: (userId: Int, isFollowed: Int) -> Unit,
    private val onBookmarkClick: (position: Int, postId: String, isSaved: Boolean) -> Unit,
    private val onEditClick: (position: Int, content: FeedResponseContentItem) -> Unit,
    private val onDeleteClick: (position: Int, postId: String) -> Unit,
    private val onLikeClick: (position: Int, postId: String, isLiked: Boolean) -> Unit,
    private val onCommentClick: (postId: String, commentCount: Int) -> Unit,
    private val videoHolder: (FeedVideoViewHolder) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val feeds = mutableListOf<FeedResponseContentItem>()

    fun setData(feeds: List<FeedResponseContentItem>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(this.feeds, feeds))
        this.feeds.clear()
        this.feeds.addAll(feeds)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addData(feeds: List<FeedResponseContentItem>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(this.feeds, feeds))
        this.feeds.addAll(feeds)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FeedType.TEXT -> FeedTextViewHolder(
                ItemPostTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.IMAGE -> FeedImageViewHolder(
                ItemPostImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.VIDEO -> FeedVideoViewHolder(
                ItemPostVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Missing view type exception ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feed = feeds[position]

        when (holder.itemViewType) {
            FeedType.TEXT -> (holder as FeedTextViewHolder).bind(
                holder.itemView,
                feed,
                userData,
                onProfileClick = { onProfileClick(it) },
                onFollowClick = { postId, isFollowed -> onFollowClick(postId, isFollowed) },
                onBookmarkClick = { postId, isSaved -> onBookmarkClick(position, postId, isSaved) },
                onEditClick = { content -> onEditClick(position, content) },
                onDeleteClick = { postId -> onDeleteClick(position, postId) },
                onLikeClick = { postId, isLiked -> onLikeClick(position, postId, isLiked) },
                onCommentClick = { postId, commentCount -> onCommentClick(postId, commentCount) }
            )
            FeedType.IMAGE -> (holder as FeedImageViewHolder).bind(
                holder.itemView,
                feed,
                userData,
                onProfileClick = { onProfileClick(it) },
                onFollowClick = { postId, isFollowed -> onFollowClick(postId, isFollowed) },
                onBookmarkClick = { postId, isSaved -> onBookmarkClick(position, postId, isSaved) },
                onEditClick = { content -> onEditClick(position, content) },
                onDeleteClick = { postId -> onDeleteClick(position, postId) },
                onLikeClick = { postId, isLiked -> onLikeClick(position, postId, isLiked) },
                onCommentClick = { postId, commentCount -> onCommentClick(postId, commentCount) }
            )
            FeedType.VIDEO -> {
                (holder as FeedVideoViewHolder).bind(
                    holder.itemView,
                    feed,
                    userData,
                    onProfileClick = { onProfileClick(it) },
                    onFollowClick = { postId, isFollowed -> onFollowClick(postId, isFollowed) },
                    onBookmarkClick = { postId, isSaved -> onBookmarkClick(position, postId, isSaved) },
                    onEditClick = { content -> onEditClick(position, content) },
                    onDeleteClick = { postId -> onDeleteClick(position, postId) },
                    onLikeClick = { postId, isLiked -> onLikeClick(position, postId, isLiked) },
                    onCommentClick = { postId, commentCount -> onCommentClick(postId, commentCount) }
                )
                videoHolder(holder)
            }
        }

    }

    override fun getItemCount(): Int = feeds.size

    override fun getItemViewType(position: Int): Int {
        val feed = feeds[position]

        return when {
            feed.linkPhoto != null -> FeedType.IMAGE
            feed.linkVideo != null -> FeedType.VIDEO
            else -> FeedType.TEXT
        }
    }
}