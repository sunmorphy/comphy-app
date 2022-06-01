package com.comphy.photo.ui.comment.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.comment.CommentResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.ActivityCommentDetailBinding
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.utils.Extension.parseTimestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import splitties.resources.drawable

@AndroidEntryPoint
class CommentDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_CONTENT = "extra_content"
        private const val EXTRA_USER_DATA = "extra_user_data"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCommentDetailBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: CommentDetailViewModel by viewModels()

    private var extraPostId: String? = null
    private var extraContentItem: CommentResponseContentItem? = null
    private var extraUserData: UserResponseData? = null

    private var commentSecondLevelAdapter: CommentSecondLevelAdapter? = null
    private var isSecondLevelReplyVisible = true
    private var parentName = ""
    private var parentId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraPostId = intent.getStringExtra(EXTRA_POST_ID)
        extraContentItem = intent.getParcelableExtra(EXTRA_CONTENT)
        extraUserData = intent.getParcelableExtra(EXTRA_USER_DATA)

        if (extraPostId == null
            || extraContentItem == null
            || extraUserData == null
        ) {
            finish()
        } else {
            setParentComment(extraContentItem!!.user.fullname, extraContentItem!!.id)
            setupObserver()
            setupWidgets()
        }
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.successResponse.observe(this) {
            commentSecondLevelAdapter!!.refresh()
        }
    }

    private fun setupWidgets() {
        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            commentSecondLevelAdapter = CommentSecondLevelAdapter { mParentName, mParentId ->
                setParentComment(mParentName, mParentId)
                edtComment.requestFocus()
            }
            rvCommentSecondLevel.apply {
                layoutManager = LinearLayoutManager(this@CommentDetailActivity)
                adapter = commentSecondLevelAdapter
            }
            lifecycleScope.launch {
                viewModel.getSecondLevelCommentPost(postId = extraPostId!!, parentId = parentId)
                    .collectLatest { commentSecondLevelAdapter!!.submitData(it) }
            }
            Glide.with(this@CommentDetailActivity)
                .load(extraContentItem!!.user.profilePhotoLink)
                .centerCrop()
                .placeholder(drawable(R.drawable.ic_placeholder_people))
                .error(drawable(R.drawable.ic_placeholder_people))
                .into(imgCommentProfile)
            txtCommentProfile.text = extraContentItem!!.user.fullname
            txtCommentJob.text = extraContentItem!!.user.job
            txtCommentContent.text = extraContentItem!!.comment
            txtCommentTimePassed.text =
                extraContentItem!!.createdDate!!.parseTimestamp(getString(R.string.placeholder_time_passed))
            txtReplyCount.text = String.format(
                getString(R.string.placeholder_comments_count),
                extraContentItem!!.secondChildComment.size.toString()
            )
            txtReplyUser.text = String.format(
                getString(R.string.placeholder_comment_reply_parent),
                parentName
            )
            Glide.with(this@CommentDetailActivity)
                .load(extraUserData!!.profileUrl)
                .centerCrop()
                .placeholder(drawable(R.drawable.ic_placeholder_people))
                .error(drawable(R.drawable.ic_placeholder_people))
                .into(imgProfile)
            layoutReplyContent.setOnClickListener {
                if (isSecondLevelReplyVisible) {
                    binding.layoutReplyContent.visibility = View.GONE
                } else {
                    binding.layoutReplyContent.visibility = View.VISIBLE
                }
                isSecondLevelReplyVisible = !isSecondLevelReplyVisible
            }
            btnSendComment.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.commentPost(
                        comment = edtComment.text.toString(),
                        parentId = parentId,
                        postId = extraPostId!!
                    )
                }
            }
            edtComment.doAfterTextChanged { btnSendComment.isEnabled = it?.isNotEmpty()!! }
        }
    }

    private fun setParentComment(mParentName: String, mParentId: Int) {
        parentName = mParentName
        parentId = mParentId

        binding.txtReplyUser.text = String.format(
            getString(R.string.placeholder_comment_reply_parent),
            parentName
        )
    }
}