package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.ItemPostImageBinding
import com.comphy.photo.utils.Extension.parseTimestamp
import com.comphy.photo.vo.FollowType.FOLLOWED
import com.comphy.photo.vo.FollowType.NOT_FOLLOWED
import com.comphy.photo.vo.FollowType.OWNED
import com.comphy.photo.vo.OrientationType
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.resources.str

class FeedImageViewHolder(
    var binding: ItemPostImageBinding
) : RecyclerView.ViewHolder(binding.root) {

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
        binding.imgFeedProfile.setOnClickListener { onProfileClick(content.userPost.id) }
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
        binding.txtFeedTimePassed.text =
            content.createdDate!!.parseTimestamp(view.resources.getString(R.string.placeholder_time_passed))
        binding.txtFeedLocation.apply {
            if (content.location != null) {
                visibility = View.VISIBLE
                text = content.location
            } else {
                visibility = View.GONE
            }
        }
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
                set.setDimensionRatio(binding.imgFeedContent.id, "1:1")
                set.applyTo(binding.root)
            }
            OrientationType.LANDSCAPE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.imgFeedContent.id, "16:9")
                set.applyTo(binding.root)
            }
            OrientationType.PORTRAIT -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.imgFeedContent.id, "4:5")
                set.applyTo(binding.root)
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

        Glide.with(view)
            .load(content.linkPhoto)
            .placeholder(view.drawable(R.drawable.img_banner_placeholder))
            .error(view.drawable(R.drawable.img_banner_placeholder))
            .format(DecodeFormat.PREFER_RGB_565)
            .centerCrop()
            .into(binding.imgFeedContent)

        binding.txtUserName.text = content.userPost.fullname
        binding.txtUserJob.text = content.userPost.job
        binding.txtFeedCaption.text = content.title
        binding.txtDataExifCamera.text = String.format(
            view.resources.getString(R.string.placeholder_camera),
            content.camera ?: "-"
        )
        binding.txtDataExifLens.text =
            String.format(view.resources.getString(R.string.placeholder_lens), content.lens ?: "-")
        binding.txtDataExifFlash.text = String.format(
            view.resources.getString(R.string.placeholder_flash),
            content.flash ?: "-"
        )
        binding.txtDataExifIso.text =
            String.format(view.resources.getString(R.string.placeholder_iso), content.iso ?: "-")
        binding.txtDataExifShutter.text = String.format(
            view.resources.getString(R.string.placeholder_shutter),
            content.shutterSpeed ?: "-"
        )
        binding.txtDataExifAperture.text = String.format(
            view.resources.getString(R.string.placeholder_aperture),
            content.aperture ?: "-"
        )
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
                content.totalLikes = if (content.liked) content.totalLikes - 1
                else content.totalLikes + 1
                content.liked = !content.liked
            }
        }
        binding.btnComment.setOnClickListener { onCommentClick(content.id, content.totalComments) }
        binding.txtCommentCount.setOnClickListener { onCommentClick(content.id, content.totalComments) }
        binding.btnCommentSeeAll.setOnClickListener { onCommentClick(content.id, content.totalComments) }
        binding.layoutComment.setOnClickListener { onCommentClick(content.id, content.totalComments) }
    }

}