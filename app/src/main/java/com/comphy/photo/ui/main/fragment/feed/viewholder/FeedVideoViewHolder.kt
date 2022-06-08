package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.ItemPostVideoBinding
import com.comphy.photo.utils.Extension.parseTimestamp
import com.comphy.photo.vo.FollowType.FOLLOWED
import com.comphy.photo.vo.FollowType.NOT_FOLLOWED
import com.comphy.photo.vo.FollowType.OWNED
import com.comphy.photo.vo.OrientationType
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.resources.str

class FeedVideoViewHolder(
    var binding: ItemPostVideoBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var player: ExoPlayer

    fun bind(
        view: View,
        content: FeedResponseContentItem,
        userData: UserResponseData,
        onProfileClick: (userId: Int) -> Unit,
        onFollowClick: (userId: Int, isFollowed: Int) -> Unit,
        onBookmarkClick: (postId: String, isSaved: Boolean) -> Unit,
        onEditClick: (content: FeedResponseContentItem) -> Unit,
        onDeleteClick: (postId: String) -> Unit,
        onLikeClick: (postId: String, isLiked: Boolean) -> Unit,
        onCommentClick: (postId: String, commentCount: Int) -> Unit
    ) {
        binding.imgFeedProfile.setOnClickListener {
            stopPlayer()
            onProfileClick(content.userPost.id)
        }
        binding.btnAddUser.apply {
            when (content.isFollowed) {
                NOT_FOLLOWED -> {
                    visibility = View.VISIBLE
                    setImageDrawable(drawable(R.drawable.ic_add_user))
                    setOnClickListener { onFollowClick(content.userPost.id, content.isFollowed) }
                }
                FOLLOWED -> {
                    visibility = View.VISIBLE
                    setImageDrawable(drawable(R.drawable.ic_remove_user))
                    setOnClickListener { onFollowClick(content.userPost.id, content.isFollowed) }
                }
                OWNED -> visibility = View.INVISIBLE
            }
        }
        binding.btnEditPost.apply {
            when (content.isFollowed) {
                OWNED -> {
                    visibility = View.VISIBLE
                    setOnClickListener { onEditClick(content) }
                }
                else -> visibility = View.INVISIBLE
            }
        }
        binding.btnBookmark.apply {
            when (content.isFollowed) {
                OWNED -> visibility = View.INVISIBLE
                else -> {
                    visibility = View.VISIBLE
                    imageTintList =
                        if (content.postSaved) colorSL(R.color.primary_orange)
                        else colorSL(R.color.black)

                    setOnClickListener {
                        onBookmarkClick(content.id, content.postSaved)
                        content.postSaved = !content.postSaved
                    }
                }
            }
        }
        binding.btnDeletePost.apply {
            when (content.isFollowed) {
                OWNED -> {
                    visibility = View.VISIBLE
                    setOnClickListener { onDeleteClick(content.id) }
                }
                else -> visibility = View.INVISIBLE
            }
        }
        binding.txtFeedLocation.apply {
            if (content.location != null) {
                visibility = View.VISIBLE
                text = String.format(
                    view.resources.getString(R.string.placeholder_post_location),
                    content.location
                )
            } else {
                visibility = View.GONE
            }
        }
        binding.txtFeedTimePassed.text =
            content.createdDate!!.parseTimestamp(view.resources.getString(R.string.placeholder_time_passed))
        binding.txtFeedCommunity.apply {
            if (content.community != null) {
                visibility = View.VISIBLE
                text = content.community.communityName
            } else {
                visibility = View.GONE
            }
        }
        when (content.orientationType) {
            OrientationType.SQUARE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.layoutFeedContent.id, "1:1")
                set.applyTo(binding.root)
            }
            OrientationType.LANDSCAPE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.layoutFeedContent.id, "16:9")
                set.applyTo(binding.root)
            }
            OrientationType.PORTRAIT -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.layoutFeedContent.id, "4:5")
                set.applyTo(binding.root)
            }
        }
        player = ExoPlayer.Builder(view.context)
            .build()
            .also {
                binding.playerFeedContent.player = it
                val mediaItem = content.linkVideo?.let { url -> MediaItem.fromUri(url) }
                if (mediaItem != null) {
                    it.setMediaItem(mediaItem)
                    it.prepare()
                }
            }

        Glide.with(view)
            .load(content.userPost.profilePhotoLink)
            .centerCrop()
            .placeholder(view.drawable(R.drawable.ic_placeholder_people))
            .error(view.drawable(R.drawable.ic_placeholder_people))
            .into(binding.imgFeedProfile)

        Glide.with(view)
            .load(userData.profilePhotoLink)
            .centerCrop()
            .placeholder(view.drawable(R.drawable.ic_placeholder_people))
            .error(view.drawable(R.drawable.ic_placeholder_people))
            .into(binding.imgProfile)

        binding.txtUserName.text = content.userPost.fullname
        binding.txtUserJob.text = content.userPost.job
        binding.txtFeedCaption.text = content.title
        binding.txtFeedDesc.text = content.description
        binding.txtLikesCount.text = String.format(
            view.resources.getString(R.string.placeholder_likes_count),
            content.totalLikes.toString()
        )
        binding.txtCommentCount.text = String.format(
            view.resources.getString(R.string.placeholder_comments_count),
            content.totalComments.toString()
        )
        binding.btnLike.apply {
            if (content.liked) {
                backgroundTintList = view.colorSL(R.color.state_button_like_reverse)
                strokeColor = view.colorSL(R.color.state_button_like_reverse)
                text = view.str(R.string.feed_liked)
            } else {
                backgroundTintList = view.colorSL(R.color.state_button_like)
                strokeColor = view.colorSL(R.color.state_button_like)
                text = view.str(R.string.feed_like)
            }
            setOnClickListener {
                stopPlayer()
                onLikeClick(content.id, content.liked)
                content.totalLikes = if (content.liked) content.totalLikes - 1
                else content.totalLikes + 1
                content.liked = !content.liked
            }
        }
        binding.btnComment.setOnClickListener {
            stopPlayer()
            onCommentClick(content.id, content.totalComments)
        }
        binding.txtCommentCount.setOnClickListener {
            stopPlayer()
            onCommentClick(content.id, content.totalComments)
        }
        binding.btnCommentSeeAll.setOnClickListener {
            stopPlayer()
            onCommentClick(content.id, content.totalComments)
        }
        binding.layoutComment.setOnClickListener {
            stopPlayer()
            onCommentClick(content.id, content.totalComments)
        }
    }

    fun stopPlayer() {
        player.stop()
    }

    fun pausePlayer() {
        player.pause()
    }

    fun releasePlayer() {
        player.release()
    }
}