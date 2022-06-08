package com.comphy.photo.ui.community.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R.*
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.databinding.BottomSheetDeletePostBinding
import com.comphy.photo.databinding.BottomSheetEditPostBinding
import com.comphy.photo.databinding.FragmentCommunityPostBinding
import com.comphy.photo.ui.comment.main.CommentActivity
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.community.detail.CommunityDetailViewModel
import com.comphy.photo.ui.community.detail.adapter.CommunitySimilarAdapter
import com.comphy.photo.ui.community.edit.EditCommunityActivity
import com.comphy.photo.ui.main.fragment.feed.FeedAdapter
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedVideoViewHolder
import com.comphy.photo.ui.post.CreatePostActivity
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.utils.Extension.loadAnim
import com.comphy.photo.vo.FollowType
import com.comphy.photo.vo.PostSource
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.resources.drawable
import splitties.toast.toast

class CommunityPostFragment : Fragment() {

    companion object {
        private const val EXTRA_DETAIL = "extra_detail"
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_COMMENT_COUNT = "extra_comment_count"
        private const val EXTRA_USER_DATA = "extra_user_data"
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_TEXT = "extra_text"
        private const val EXTRA_IMAGE = "extra_image"
        private const val EXTRA_VIDEO = "extra_video"
    }

    private var _binding: FragmentCommunityPostBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetEditPostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetEditPostBinding.inflate(layoutInflater)
    }
    private val bottomSheetDeletePostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDeletePostBinding.inflate(layoutInflater)
    }
    private val viewModel: CommunityDetailViewModel by activityViewModels()
    private var feedAdapter: FeedAdapter? = null
    private var feedAdapterItemCount: Int? = null
    private var communitySimilarAdapter: CommunitySimilarAdapter? = null
    private var videoViewHolder: FeedVideoViewHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentItem = (activity as CommunityDetailActivity).contentItem

        lifecycleScope.launch {
            viewModel.getSimilarCommunity(contentItem!!.categoryCommunity.id)
            viewModel.getCommunityPosts(contentItem.id)
        }

        Glide.with(this)
            .load(contentItem!!.profilePhotoCommunityLink)
            .placeholder(drawable(drawable.ic_placeholder_people))
            .error(drawable(drawable.ic_placeholder_people))
            .centerCrop()
            .into(binding.imgAdminProfile)

        binding.txtCommunitySettings.setOnClickListener {
            start<EditCommunityActivity> {
                putExtra(EXTRA_CONTENT_ITEM, (activity as CommunityDetailActivity).contentItem)
            }
        }
        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.isDeletePostLoading.observe(viewLifecycleOwner) {
            setBottomSheetDeletePostLoading(it)
        }
        viewModel.userData.observe(viewLifecycleOwner) {
            feedAdapter = FeedAdapter(
                userData = it,
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
                        putExtra(EXTRA_USER_DATA, it)
                    }
                },
                videoHolder = { holder ->
                    videoViewHolder = holder
                    binding.rvCommunityPost.addOnScrollListener(object :
                        RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            holder.pausePlayer()
                            super.onScrolled(recyclerView, dx, dy)
                        }

                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            holder.pausePlayer()
                            super.onScrollStateChanged(recyclerView, newState)
                        }
                    })
                    (activity as CommunityDetailActivity).binding.mainView.setOnScrollChangeListener(
                        NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                            holder.pausePlayer()
                        }
                    )
                }
            )
        }
        viewModel.communityPosts.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                feedAdapter!!.submitData(it)
                feedAdapter!!.loadStateFlow.collect {
                    feedAdapterItemCount = feedAdapter!!.itemCount
                }
            }
            setupRecycler()
        }
        viewModel.similarCommunity.observe(viewLifecycleOwner) {
            communitySimilarAdapter = CommunitySimilarAdapter {
                start<CommunityDetailActivity> { putExtra(EXTRA_DETAIL, it) }
            }
            communitySimilarAdapter!!.setData(it)
        }
        viewModel.successResponse.observe(viewLifecycleOwner) {
            (activity as CommunityDetailActivity).bottomSheetDialog.dismiss()
            feedAdapter!!.notifyItemChanged(it)
            binding.rvCommunityPost.clearFocus()
        }
        viewModel.followResponse.observe(viewLifecycleOwner) {
            feedAdapter!!.refresh()
            binding.rvCommunityPost.clearFocus()
        }
        viewModel.deleteResponse.observe(viewLifecycleOwner) {
            (activity as CommunityDetailActivity).bottomSheetDialog.dismiss()
            feedAdapter!!.refresh()
            binding.rvCommunityPost.clearFocus()
        }
        viewModel.errorNorException.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun setupRecycler() {
        if (feedAdapterItemCount == 0) {
            binding.txtUserCreateCommunity.visibility = VISIBLE
            binding.layoutCommunityCreated.visibility = VISIBLE
            binding.txtCommunitySettings.setOnClickListener {
                start<EditCommunityActivity> {
                    putExtra(EXTRA_CONTENT_ITEM, (activity as CommunityDetailActivity).contentItem)
                }
            }
        } else {
            binding.txtUserCreateCommunity.visibility = GONE
            binding.layoutCommunityCreated.visibility = GONE
            binding.rvCommunityPost.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = feedAdapter
            }
        }
        binding.rvSimilarCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = communitySimilarAdapter
        }
    }

    private fun setupWidgets() {
        if ((activity as CommunityDetailActivity).contentItem!!.joined) {
            binding.txtUserCreateCommunity.visibility = GONE
            binding.layoutCommunityCreated.visibility = GONE
        } else {
            if (feedAdapterItemCount == 0) {
                binding.txtUserCreateCommunity.visibility = VISIBLE
                binding.layoutCommunityCreated.visibility = VISIBLE
            } else {
                binding.txtUserCreateCommunity.visibility = GONE
                binding.layoutCommunityCreated.visibility = GONE
            }
        }
        binding.btnSeeAllSimilar.setOnClickListener {
            toast("THIS FEATURE IS NOT READY YET")
        }
        binding.btnDismiss.setOnClickListener { binding.layoutSimilarCommunity.visibility = GONE }
        binding.edtWritePost.setOnClickListener {
            start<CreatePostActivity> {
                putExtra(EXTRA_TEXT, EXTRA_TEXT)
                putExtra(EXTRA_SOURCE, PostSource.SOURCE_COMMUNITY)
            }
        }
        binding.btnUploadImage.setOnClickListener {
            start<CreatePostActivity> {
                putExtra(EXTRA_IMAGE, EXTRA_IMAGE)
                putExtra(EXTRA_SOURCE, PostSource.SOURCE_COMMUNITY)
            }
        }
        binding.btnUploadVideo.setOnClickListener {
            start<CreatePostActivity> {
                putExtra(EXTRA_VIDEO, EXTRA_VIDEO)
                putExtra(EXTRA_SOURCE, PostSource.SOURCE_COMMUNITY)
            }
        }
    }

    private fun showBottomSheetEditPost(content: FeedResponseContentItem, position: Int) {
        with(bottomSheetEditPostBinding) {
            edtPostTitle.setText(content.title)
            edtPostTitle.doAfterTextChanged {
                txtPostTitleMax.text = String.format(
                    getString(string.placeholder_post_title_count),
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
                        if (edt.text.toString() == dataExif[inputDataExifWidgets.indexOf(edt)])
                            return true
                    }
                    return false
                }

                layoutCaption.visibility = INVISIBLE
                layoutDataExif.visibility = VISIBLE

                edtPostCamera.setText(content.camera)
                edtPostIso.setText(content.iso)
                edtPostLens.setText(content.lens)
                edtPostShutter.setText(content.shutterSpeed)
                edtPostFlash.setText(content.flash)
                edtPostAperture.setText(content.aperture)

                edtPostTitle.doAfterTextChanged {
                    btnSaveChange.isEnabled =
                        (edtPostTitle.text.toString() != content.title
                                && edtPostTitle.text.isNotEmpty()
                                || !isFieldNotNecessary())
                }
                inputDataExifWidgets.forEach {
                    it.doAfterTextChanged {
                        btnSaveChange.isEnabled =
                            (edtPostTitle.text.toString() != content.title
                                    && edtPostTitle.text.isNotEmpty()
                                    || !isFieldNotNecessary())
                    }
                }
                btnSaveChange.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.updatePost(
                            postId = content.id,
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

                layoutCaption.visibility = VISIBLE
                layoutDataExif.visibility = INVISIBLE

                fun isFieldNotNecessary(): Boolean {
                    inputWidgets.forEach { edt ->
                        if (edt.text.toString() != inputData[inputWidgets.indexOf(edt)]
                            && edt.text.isEmpty()
                        ) return false
                    }
                    return true
                }

                edtPostCaption.setText(content.description)

                inputWidgets.forEach {
                    it.doAfterTextChanged { btnSaveChange.isEnabled = !isFieldNotNecessary() }
                }
                btnSaveChange.setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.updatePost(
                            postId = content.id,
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
        (activity as CommunityDetailActivity).bottomSheetDialog.setContentView(
            bottomSheetEditPostBinding.root
        )
        (activity as CommunityDetailActivity).bottomSheetDialog.show()
    }

    private fun showBottomSheetDeletePost(postId: String, position: Int) {
        with(bottomSheetDeletePostBinding) {
            btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deletePost(postId, position)
                }
            }
            btnCancel.setOnClickListener { (activity as CommunityDetailActivity).bottomSheetDialog.dismiss() }
        }
        (activity as CommunityDetailActivity).bottomSheetDialog.setContentView(
            bottomSheetDeletePostBinding.root
        )
        (activity as CommunityDetailActivity).bottomSheetDialog.show()
    }

    private fun setBottomSheetDeletePostLoading(state: Boolean) {
        with(bottomSheetDeletePostBinding) {
            if (state) {
                btnDelete.visibility = INVISIBLE
                imgLoadingBtn.apply {
                    visibility = VISIBLE
                    startAnimation(activity?.loadAnim(anim.btn_loading_anim))
                }
            } else {
                btnDelete.visibility = VISIBLE
                imgLoadingBtn.apply {
                    clearAnimation()
                    visibility = GONE
                }
            }
            (activity as CommunityDetailActivity).bottomSheetDialog.setCancelable(!state)
            btnCancel.isEnabled = !state
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as CommunityDetailActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }

    override fun onDestroyView() {
        videoViewHolder?.stopPlayer()
        videoViewHolder?.releasePlayer()
        communitySimilarAdapter = null

        super.onDestroyView()
    }

    override fun onDestroy() {
        videoViewHolder?.stopPlayer()
        videoViewHolder?.releasePlayer()
        communitySimilarAdapter = null

        super.onDestroy()
    }

    override fun onStop() {
        videoViewHolder?.pausePlayer()

        super.onStop()
    }
}