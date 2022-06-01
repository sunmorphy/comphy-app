package com.comphy.photo.ui.search.explore.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.databinding.ActivityExploreDetailBinding
import com.comphy.photo.ui.comment.main.CommentActivity
import com.comphy.photo.ui.main.fragment.feed.fragment.FeedMainFragment
import com.comphy.photo.utils.Extension.parseTimestamp
import com.comphy.photo.vo.FollowType
import com.comphy.photo.vo.OrientationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.fragments.start
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.resources.str

@AndroidEntryPoint
class ExploreDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_COMMENT_COUNT = "extra_comment_count"
        private const val EXTRA_USER_DATA = "extra_user_data"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExploreDetailBinding.inflate(layoutInflater)
    }
    private val viewModel: ExploreDetailViewModel by viewModels()
    private var contentItem: FeedResponseContentItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        contentItem = intent.getParcelableExtra(EXTRA_CONTENT_ITEM)
        if (contentItem == null) finish()
        else setupWidgets(contentItem!!)
    }

    private fun setupWidgets(contentItem: FeedResponseContentItem) {
        with(binding) {
            imgFeedProfile.setOnClickListener {
                // TODO: INTENT TO USER PROFILE DETAIL
            }
            btnAddUser.apply {
                when (contentItem.isFollowed) {
                    FollowType.NOT_FOLLOWED -> {
                        setImageDrawable(drawable(R.drawable.ic_add_user))
                        setOnClickListener {
                            lifecycleScope.launch {
                                viewModel.followUser(
                                    contentItem.userPost.id
                                )
                            }
                        }
                    }
                    FollowType.FOLLOWED -> {
                        setImageDrawable(drawable(R.drawable.ic_remove_user))
                        setOnClickListener {
                            lifecycleScope.launch {
                                viewModel.unfollowUser(
                                    contentItem.userPost.id
                                )
                            }
                        }
                    }
                    FollowType.OWNED -> visibility = View.INVISIBLE
                }
            }
            btnBookmark.apply {
                imageTintList =
                    if (contentItem.postSaved) colorSL(R.color.primary_orange)
                    else colorSL(R.color.black)

                setOnClickListener {
                    lifecycleScope.launch {
                        if (contentItem.postSaved) {
                            viewModel.unbookmarkPost(contentItem.id)
                        } else {
                            viewModel.bookmarkPost(contentItem.id)
                        }
                    }
                    contentItem.postSaved = !contentItem.postSaved
                }
            }
            txtFeedTimePassed.text =
                contentItem.createdDate!!.parseTimestamp(getString(R.string.placeholder_time_passed))
            txtFeedLocation.apply {
                if (contentItem.location != null) {
                    visibility = View.VISIBLE
                    text = contentItem.location
                } else {
                    visibility = View.GONE
                }
            }
            txtFeedCommunity.apply {
                if (contentItem.community != null) {
                    visibility = View.VISIBLE
                    text = String.format(
                        getString(R.string.placeholder_post_location),
                        contentItem.community
                    )
                } else {
                    visibility = View.GONE
                }
            }
            when (contentItem.orientationType) {
                OrientationType.SQUARE -> {
                    val set = ConstraintSet()
                    set.clone(root)
                    set.setDimensionRatio(imgFeedContent.id, "1:1")
                    set.applyTo(root)
                }
                OrientationType.LANDSCAPE -> {
                    val set = ConstraintSet()
                    set.clone(root)
                    set.setDimensionRatio(imgFeedContent.id, "16:9")
                    set.applyTo(root)
                }
                OrientationType.PORTRAIT -> {
                    val set = ConstraintSet()
                    set.clone(root)
                    set.setDimensionRatio(imgFeedContent.id, "4:5")
                    set.applyTo(root)
                }
            }
            Glide.with(this@ExploreDetailActivity)
                .load(contentItem.userPost.profilePhotoLink)
                .centerCrop()
                .placeholder(drawable(R.drawable.ic_placeholder_people))
                .error(drawable(R.drawable.ic_placeholder_people))
                .into(imgFeedProfile)

//            Glide.with(this@ExploreDetailActivity)
//                .load(userData.profileUrl)
//                .centerCrop()
//                .placeholder(drawable(R.drawable.ic_placeholder_people))
//                .error(drawable(R.drawable.ic_placeholder_people))
//                .into(imgProfile)

            Glide.with(this@ExploreDetailActivity)
                .load(contentItem.linkPhoto)
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .into(imgFeedContent)

            txtUserName.text = contentItem.userPost.fullname
            txtUserJob.text = contentItem.userPost.job
            txtFeedCaption.text = contentItem.title
            txtDataExifCamera.text = String.format(
                getString(R.string.placeholder_camera),
                contentItem.camera ?: "-"
            )
            txtDataExifLens.text =
                String.format(getString(R.string.placeholder_lens), contentItem.lens ?: "-")
            binding.txtDataExifFlash.text = String.format(
                getString(R.string.placeholder_flash),
                contentItem.flash ?: "-"
            )
            txtDataExifIso.text =
                String.format(getString(R.string.placeholder_iso), contentItem.iso ?: "-")
            txtDataExifShutter.text = String.format(
                getString(R.string.placeholder_shutter),
                contentItem.shutterSpeed ?: "-"
            )
            binding.txtDataExifAperture.text = String.format(
                getString(R.string.placeholder_aperture),
                contentItem.aperture ?: "-"
            )
            txtLikesCount.text = String.format(
                getString(R.string.placeholder_likes_count),
                contentItem.totalLikes.toString()
            )
            txtCommentCount.text = String.format(
                getString(R.string.placeholder_comments_count),
                contentItem.totalComments.toString()
            )
            btnLike.apply {
                if (contentItem.liked) {
                    backgroundTintList = colorSL(R.color.state_button_like_reverse)
                    strokeColor = colorSL(R.color.state_button_like_reverse)
                    text = str(R.string.feed_liked)
                } else {
                    backgroundTintList = colorSL(R.color.state_button_like)
                    strokeColor = colorSL(R.color.state_button_like)
                    text = str(R.string.feed_like)
                }
                setOnClickListener {
                    lifecycleScope.launch {
                        if (contentItem.liked) {
                            viewModel.unlikePost(contentItem.id)
                        } else {
                            viewModel.likePost(contentItem.id)
                        }
                    }
                    contentItem.liked = !contentItem.liked
                }
            }
            btnComment.setOnClickListener {
                start<CommentActivity> {
                    putExtra(EXTRA_POST_ID, contentItem.id)
                    putExtra(EXTRA_COMMENT_COUNT, contentItem.totalComments)
//                    putExtra(EXTRA_USER_DATA, userData)
                }
            }
            txtCommentCount.setOnClickListener {
                start<CommentActivity> {
                    putExtra(EXTRA_POST_ID, contentItem.id)
                    putExtra(EXTRA_COMMENT_COUNT, contentItem.totalComments)
//                    putExtra(EXTRA_USER_DATA, userData)
                }
            }
            btnCommentSeeAll.setOnClickListener {
                start<CommentActivity> {
                    putExtra(EXTRA_POST_ID, contentItem.id)
                    putExtra(EXTRA_COMMENT_COUNT, contentItem.totalComments)
//                    putExtra(EXTRA_USER_DATA, userData)
                }
            }
            layoutComment.setOnClickListener {
                start<CommentActivity> {
                    putExtra(EXTRA_POST_ID, contentItem.id)
                    putExtra(EXTRA_COMMENT_COUNT, contentItem.totalComments)
//                    putExtra(EXTRA_USER_DATA, userData)
                }
            }
        }
    }
}