package com.comphy.photo.ui.main.fragment.feed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.BottomSheetDeletePostBinding
import com.comphy.photo.databinding.BottomSheetEditPostBinding
import com.comphy.photo.databinding.FragmentFeedMainBinding
import com.comphy.photo.ui.comment.main.CommentActivity
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.ui.main.MainViewModel
import com.comphy.photo.ui.main.fragment.feed.FeedAdapter
import com.comphy.photo.ui.main.fragment.feed.FeedFragment
import com.comphy.photo.ui.main.fragment.feed.FeedViewModel
import com.comphy.photo.ui.main.fragment.feed.FeedsAdapter
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedVideoViewHolder
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.utils.Extension.loadAnim
import com.comphy.photo.vo.FollowType
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.toast.toast

class FeedMainFragment : Fragment() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_COMMENT_COUNT = "extra_comment_count"
        private const val EXTRA_USER_DATA = "extra_user_data"
    }

    private var _binding: FragmentFeedMainBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetEditPostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetEditPostBinding.inflate(layoutInflater)
    }
    private val bottomSheetDeletePostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDeletePostBinding.inflate(layoutInflater)
    }
    private val viewModel: FeedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var feedsAdapter: FeedsAdapter? = null
    private var videoViewHolder: FeedVideoViewHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getFeeds() }
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.userData.observe(viewLifecycleOwner) { setupRecyclerView(it) }
        viewModel.feedsResponse.observe(viewLifecycleOwner) { feedsAdapter!!.setData(it) }
        viewModel.isDeletePostLoading.observe(viewLifecycleOwner) {
            setBottomSheetDeletePostLoading(it)
        }
//        viewModel.filterResponse.observe(viewLifecycleOwner) {
//            lifecycleScope.launch {
//                feedAdapter!!.submitData(PagingData.empty())
//                feedAdapter!!.submitData(it)
//            }
//        }
        viewModel.successResponse.observe(viewLifecycleOwner) {
            feedsAdapter!!.notifyItemChanged(it)
            binding.rvFeed.clearFocus()
        }
        viewModel.followResponse.observe(viewLifecycleOwner) {
//            lifecycleScope.launch { viewModel.getFeeds() }
            binding.rvFeed.invalidate()
            binding.rvFeed.clearFocus()
        }
        viewModel.deleteResponse.observe(viewLifecycleOwner) {
            feedsAdapter!!.feeds.removeAt(it)
            feedsAdapter!!.notifyItemRemoved(it)
            binding.rvFeed.clearFocus()
        }
        viewModel.errorNorException.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun setupRecyclerView(userData: UserResponseData) {
        feedsAdapter = FeedsAdapter(
            userData = userData,
            onProfileClick = { start<ProfileActivity> { putExtra(EXTRA_ID, it) } },
            onFollowClick = { userId, isFollowed ->
                lifecycleScope.launch {
                    when (isFollowed) {
                        FollowType.NOT_FOLLOWED -> viewModel.followUser(userId)
                        FollowType.FOLLOWED -> viewModel.unfollowUser(userId)
                    }
                }
            },
            onBookmarkClick = { pos, postId, isSaved ->
                lifecycleScope.launch {
                    if (isSaved) viewModel.unbookmarkUser(postId, pos)
                    else viewModel.bookmarkUser(postId, pos)
                }
            },
            onEditClick = { pos, content -> showBottomSheetEditPost(content, pos) },
            onDeleteClick = { pos, postId -> showBottomSheetDeletePost(postId, pos) },
            onLikeClick = { pos, postId, isLiked ->
                lifecycleScope.launch {
                    if (isLiked) viewModel.unlikePost(postId, pos)
                    else viewModel.likePost(postId, pos)
                }
            },
            onCommentClick = { postId, commentCount ->
                start<CommentActivity> {
                    putExtra(EXTRA_POST_ID, postId)
                    putExtra(EXTRA_COMMENT_COUNT, commentCount)
                    putExtra(EXTRA_USER_DATA, userData)
                }
            },
            videoHolder = { holder ->
                videoViewHolder = holder
                binding.rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        holder.pausePlayer()
                        super.onScrolled(recyclerView, dx, dy)
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        holder.pausePlayer()
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
                (parentFragment as FeedFragment).binding.mainView.setOnScrollChangeListener(
                    NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                        holder.pausePlayer()
                    }
                )
            }
        )

        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = feedsAdapter
            setHasFixedSize(false)
        }

//        lifecycleScope.launch {
//            viewModel.getFeedPost().collectLatest { feedAdapter!!.submitData(it) }
//        }
    }

    private fun showBottomSheetEditPost(content: FeedResponseContentItem, position: Int) {
        with(bottomSheetEditPostBinding) {
            edtPostTitle.setText(content.title)
            edtPostTitle.doAfterTextChanged {
                txtPostTitleMax.text = String.format(
                    getString(R.string.placeholder_post_title_count),
                    edtPostTitle.text.length.toString()
                )
            }

            if (content.linkPhoto != null) {
                val inputDataExifWidgets = listOf(
                    edtPostCamera,
                    edtPostIso,
                    edtPostLens,
                    edtPostShutter,
                    edtPostFlash,
                    edtPostAperture
                )
                val dataExif = listOf(
                    content.camera,
                    content.iso,
                    content.lens,
                    content.shutterSpeed,
                    content.flash,
                    content.aperture
                )

                fun isFieldNotNecessary(): Boolean {
                    inputDataExifWidgets.forEach { edt ->
                        if (
                            edt.text.toString() == dataExif[inputDataExifWidgets.indexOf(edt)]
                        ) return true
                    }
                    return false
                }

                layoutCaption.visibility = View.INVISIBLE
                layoutDataExif.visibility = View.VISIBLE

                edtPostCamera.setText(content.camera)
                edtPostIso.setText(content.iso)
                edtPostLens.setText(content.lens)
                edtPostShutter.setText(content.shutterSpeed)
                edtPostFlash.setText(content.flash)
                edtPostAperture.setText(content.aperture)

                edtPostTitle.doAfterTextChanged {
                    btnSaveChange.isEnabled =
                        (edtPostTitle.text.toString() != content.title && edtPostTitle.text.isNotEmpty() && !isFieldNotNecessary())
                }
                inputDataExifWidgets.forEach {
                    it.doAfterTextChanged {
                        btnSaveChange.isEnabled =
                            (edtPostTitle.text.toString() != content.title && edtPostTitle.text.isNotEmpty() && !isFieldNotNecessary())
                    }
                }
                btnSaveChange.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.updatePost(
                            title = edtPostTitle.text.toString(),
                            description = content.description,
                            orientationType = content.orientationType,
                            iso = edtPostIso.text.toString(),
                            lens = edtPostLens.text.toString(),
                            shutterSpeed = edtPostShutter.text.toString(),
                            aperture = edtPostAperture.text.toString(),
                            camera = edtPostCamera.text.toString(),
                            flash = edtPostFlash.text.toString(),
                            location = content.location,
                            categoryCommunityId = content.categoryCommunity.id,
                            communityId = content.community?.id,
                            linkPhoto = content.linkPhoto,
                            linkVideo = content.linkVideo,
                            position = position
                        )
                    }
                    content.title = edtPostTitle.text.toString()
                    content.iso = edtPostIso.text.toString()
                    content.lens = edtPostLens.text.toString()
                    content.shutterSpeed = edtPostShutter.text.toString()
                    content.aperture = edtPostAperture.text.toString()
                    content.camera = edtPostCamera.text.toString()
                    content.flash = edtPostFlash.text.toString()
                }

            } else {
                val inputWidgets = listOf(edtPostTitle, edtPostCaption)
                val inputData = listOf(content.title, content.description)

                layoutCaption.visibility = View.VISIBLE
                layoutDataExif.visibility = View.INVISIBLE

                fun isFieldNotNecessary(): Boolean {
                    inputWidgets.forEach { edt ->
                        if (edt.text.toString() == inputData[inputWidgets.indexOf(edt)]
                            && edt.text.isEmpty()
                        ) return true
                    }
                    return false
                }

                edtPostCaption.setText(content.description)

                inputWidgets.forEach {
                    it.doAfterTextChanged { btnSaveChange.isEnabled = !isFieldNotNecessary() }
                }
                btnSaveChange.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.updatePost(
                            title = edtPostTitle.text.toString(),
                            description = edtPostCaption.text.toString(),
                            orientationType = content.orientationType,
                            iso = content.iso,
                            lens = content.lens,
                            shutterSpeed = content.shutterSpeed,
                            aperture = content.aperture,
                            camera = content.camera,
                            flash = content.flash,
                            location = content.location,
                            categoryCommunityId = content.categoryCommunity.id,
                            communityId = content.community?.id,
                            linkPhoto = content.linkPhoto,
                            linkVideo = content.linkVideo,
                            position = position
                        )
                    }
                    content.title = edtPostTitle.text.toString()
                    content.description = edtPostCaption.text.toString()
                }
            }
        }
        (activity as MainActivity).bottomSheetDialog.setContentView(bottomSheetEditPostBinding.root)
        (activity as MainActivity).bottomSheetDialog.show()
    }

    private fun showBottomSheetDeletePost(postId: String, position: Int) {
        with(bottomSheetDeletePostBinding) {
            btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deletePost(
                        postId,
                        position
                    )
                }
            }
            btnCancel.setOnClickListener { (activity as MainActivity).bottomSheetDialog.dismiss() }
        }
        (activity as MainActivity).bottomSheetDialog.setContentView(bottomSheetDeletePostBinding.root)
        (activity as MainActivity).bottomSheetDialog.show()
    }

    private fun setBottomSheetDeletePostLoading(state: Boolean) {
        with(bottomSheetDeletePostBinding) {
            if (state) {
                btnDelete.visibility = View.INVISIBLE
                imgLoadingBtn.apply {
                    visibility = View.VISIBLE
                    startAnimation(activity?.loadAnim(R.anim.btn_loading_anim))
                }
            } else {
                btnDelete.visibility = View.VISIBLE
                imgLoadingBtn.apply {
                    clearAnimation()
                    visibility = View.GONE
                }
            }
            (activity as MainActivity).bottomSheetDialog.setCancelable(!state)
            btnCancel.isEnabled = !state
        }
    }

    override fun onResume() {
        super.onResume()
        if ((parentFragment as FeedFragment).shouldRefresh) {
            lifecycleScope.launch { viewModel.getFeeds() }
            (parentFragment as FeedFragment).shouldRefresh = false
        }
    }

    override fun onDestroyView() {
        videoViewHolder?.stopPlayer()
        videoViewHolder?.releasePlayer()
        feedsAdapter = null

        super.onDestroyView()
    }

    override fun onDestroy() {
        videoViewHolder?.stopPlayer()
        videoViewHolder?.releasePlayer()
        feedsAdapter = null

        super.onDestroy()
    }

    override fun onStop() {
        videoViewHolder?.pausePlayer()

        super.onStop()
    }
}