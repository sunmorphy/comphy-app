package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.ItemPostTextBinding
import com.comphy.photo.utils.Extension.parseTimestamp
import com.comphy.photo.vo.FollowType.FOLLOWED
import com.comphy.photo.vo.FollowType.NOT_FOLLOWED
import com.comphy.photo.vo.FollowType.OWNED
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.resources.str
import java.text.SimpleDateFormat
import java.util.*

class FeedTextViewHolder(
    var binding: ItemPostTextBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        view: View,
        content: FeedResponseContentItem,
        userData: UserResponseData,
        onProfileClick: (userId: Int) -> Unit,
        onFollowClick: (userId: Int, isFollowed: Int) -> Unit,
        onBookmarkClick: (postId: String, isSaved: Boolean) -> Unit,
        onLikeClick: (postId: String, isLiked: Boolean) -> Unit,
        onCommentClick: (postId: String, commentCount: Int) -> Unit
    ) {
        binding.imgFeedProfile.setOnClickListener { onProfileClick(content.userPost.id) }
        binding.btnAddUser.apply {
            when (content.isFollowed) {
                NOT_FOLLOWED -> {
                    setImageDrawable(drawable(R.drawable.ic_add_user))
                    setOnClickListener { onFollowClick(content.userPost.id, content.isFollowed) }
                }
                FOLLOWED -> {
                    setImageDrawable(drawable(R.drawable.ic_remove_user))
                    setOnClickListener { onFollowClick(content.userPost.id, content.isFollowed) }
                }
                OWNED -> visibility = View.INVISIBLE
            }
        }
        binding.btnBookmark.apply {
            imageTintList =
                if (content.postSaved) colorSL(R.color.primary_orange)
                else colorSL(R.color.black)

            setOnClickListener {
                onBookmarkClick(content.id, content.postSaved)
                content.postSaved = !content.postSaved
            }
        }
        binding.txtFeedTimePassed.text =
            content.createdDate!!.parseTimestamp(view.resources.getString(R.string.placeholder_time_passed))
        binding.txtFeedLocation.apply {
            if (content.community != null) {
                visibility = View.VISIBLE
                text = String.format(
                    view.resources.getString(R.string.placeholder_post_location),
                    content.location
                )
            } else {
                visibility = View.GONE
            }
        }
        binding.txtFeedCommunity.apply {
            if (content.community != null) {
                visibility = View.VISIBLE
                text = content.community
            } else {
                visibility = View.GONE
            }
        }
        Glide.with(view)
            .load(content.userPost.profilePhotoLink)
            .centerCrop()
            .placeholder(view.drawable(R.drawable.ic_placeholder_people))
            .error(view.drawable(R.drawable.ic_placeholder_people))
            .into(binding.imgFeedProfile)

        Glide.with(view)
            .load(userData.profileUrl)
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
                onLikeClick(content.id, content.liked)
                content.liked = !content.liked
            }
        }
        binding.btnComment.setOnClickListener { onCommentClick(content.id, content.totalComments) }
        binding.txtCommentCount.setOnClickListener { onCommentClick(content.id, content.totalComments) }
        binding.btnCommentSeeAll.setOnClickListener { onCommentClick(content.id, content.totalComments) }
        binding.layoutComment.setOnClickListener { onCommentClick(content.id, content.totalComments) }
    }
}