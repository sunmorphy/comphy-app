package com.comphy.photo.ui.comment.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.ActivityCommentBinding
import com.comphy.photo.ui.comment.detail.CommentDetailActivity
import com.comphy.photo.ui.custom.CustomLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.resources.drawable

@AndroidEntryPoint
class CommentActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_COMMENT_COUNT = "extra_comment_count"
        private const val EXTRA_CONTENT = "extra_content"
        private const val EXTRA_USER_DATA = "extra_user_data"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCommentBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: CommentViewModel by viewModels()

    private var extraPostId: String? = null
    private var extraCommentCount: Int = -1
    private var extraUserData: UserResponseData? = null

    private var commentLayoutManager: LinearLayoutManager? = null
    private var commentAdapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraPostId = intent.getStringExtra(EXTRA_POST_ID)
        extraCommentCount = intent.getIntExtra(EXTRA_COMMENT_COUNT, -1)
        extraUserData = intent.getParcelableExtra(EXTRA_USER_DATA)

        setupObserver()

        if (extraPostId == null || extraCommentCount == -1 || extraUserData == null) {
            finish()
        } else {
            setupWidgets()
        }
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.commentResponse.observe(this) {
        }

        viewModel.successResponse.observe(this) {
            commentAdapter!!.refresh()
//            binding.rvComment.adapter = commentAdapter
//            binding.rvComment.adapter!!.notifyDataSetChanged()
//            binding.rvComment.adapter!!.notifyItemRangeChanged(
//                0,
//                contentItems!!.size
//            )
        }
    }

    private fun setupWidgets() {
        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            commentAdapter = CommentAdapter { content ->
                start<CommentDetailActivity> {
                    putExtra(EXTRA_POST_ID, extraPostId!!)
                    putExtra(EXTRA_CONTENT, content)
                    putExtra(EXTRA_USER_DATA, extraUserData)
                }
            }

            lifecycleScope.launch {
                viewModel.getCommentPost(postId = extraPostId!!)
                    .collectLatest { commentAdapter!!.submitData(it) }
            }

            binding.rvComment.apply {
                layoutManager = LinearLayoutManager(this@CommentActivity)
                adapter = commentAdapter
                setHasFixedSize(true)
            }

            binding.txtCommentCount.text = String.format(
                getString(R.string.placeholder_comments_count),
                extraCommentCount.toString()
            )

            Glide.with(this@CommentActivity)
                .load(extraUserData!!.profileUrl)
                .centerCrop()
                .placeholder(drawable(R.drawable.ic_placeholder_people))
                .error(drawable(R.drawable.ic_placeholder_people))
                .into(imgProfile)

            btnSendComment.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.commentPost(edtComment.text.toString(), extraPostId!!)
                }
            }

            edtComment.doAfterTextChanged { btnSendComment.isEnabled = it?.isNotEmpty()!! }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        commentLayoutManager = null
        commentAdapter = null
    }
}