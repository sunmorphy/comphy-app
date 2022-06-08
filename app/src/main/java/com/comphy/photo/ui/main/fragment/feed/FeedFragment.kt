package com.comphy.photo.ui.main.fragment.feed

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.RadioButton
import android.widget.ScrollView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.*
import com.comphy.photo.ui.comment.main.CommentActivity
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.ui.main.MainViewModel
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedVideoViewHolder
import com.comphy.photo.ui.post.CreatePostActivity
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.search.explore.main.ExploreActivity
import com.comphy.photo.utils.Extension.formatLocationInput
import com.comphy.photo.utils.Extension.loadAnim
import com.comphy.photo.vo.FollowType
import com.comphy.photo.vo.PostSource
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import splitties.dimensions.dp
import splitties.fragments.start
import splitties.resources.color
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.toast.toast


class FeedFragment : Fragment() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_COMMENT_COUNT = "extra_comment_count"
        private const val EXTRA_USER_DATA = "extra_user_data"
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_TEXT = "extra_text"
        private const val EXTRA_IMAGE = "extra_image"
        private const val EXTRA_VIDEO = "extra_video"
    }

    private var _binding: FragmentFeedBinding? = null
    val binding get() = _binding!!

    private val viewModel: FeedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val dialog by lazy(LazyThreadSafetyMode.NONE) { Dialog(requireContext()) }
    private val dialogFeedOptionsBinding by lazy(LazyThreadSafetyMode.NONE) {
        DialogFeedOptionsBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(requireContext()) }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetFilterPostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFilterPostBinding.inflate(layoutInflater)
    }
    private val bottomSheetEditPostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetEditPostBinding.inflate(layoutInflater)
    }
    private val bottomSheetDeletePostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDeletePostBinding.inflate(layoutInflater)
    }
    private var feedsAdapter: FeedsAdapter? = null
    private var videoViewHolder: FeedVideoViewHolder? = null
    private val categories = mutableListOf<String>()
    private val categoryIds = mutableListOf<Int>()
    private var isCanceled = true
    private var checkedButtonId = -1
    var shouldRefresh = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getFeeds()
            viewModel.getCommunityCategories()
        }
        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.isDeletePostLoading.observe(viewLifecycleOwner) {
            setBottomSheetDeletePostLoading(it)
        }
        viewModel.isFetching.observe(viewLifecycleOwner) { customLoading.showLoading(it) }
        mainViewModel.userData.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
            binding.txtLocation.text = formatLocationInput(it.location!!)
            Glide.with(this)
                .load(it.profilePhotoLink)
                .placeholder(activity?.drawable(R.drawable.ic_placeholder_people))
                .error(activity?.drawable(R.drawable.ic_placeholder_people))
                .centerCrop()
                .into(binding.imgProfile)
        }
        viewModel.feedsResponse.observe(viewLifecycleOwner) { feedsAdapter!!.setData(it) }
        viewModel.categories.observe(viewLifecycleOwner) {
            it.forEach { category ->
                categories.add(category.name)
                categoryIds.add(category.id)

                val rbCategory = RadioButton(requireContext())
                rbCategory.id = View.generateViewId()
                rbCategory.text = category.name
                rbCategory.setTextColor(color(R.color.radio_text))
                rbCategory.setPaddingRelative(
                    requireContext().dp(23),
                    requireContext().dp(8),
                    requireContext().dp(23),
                    requireContext().dp(8)
                )
                rbCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                rbCategory.typeface =
                    ResourcesCompat.getFont(requireContext(), R.font.poppins_medium)
                rbCategory.buttonDrawable = drawable(R.drawable.state_radio)
                rbCategory.buttonTintList = colorSL(R.color.primary_orange)
                if (category.name.lowercase().contains("semua")) rbCategory.isChecked = true
                bottomSheetFilterPostBinding.rgFilterCategory.addView(rbCategory)
            }
            checkedButtonId = bottomSheetFilterPostBinding.rgFilterCategory.checkedRadioButtonId
        }
        viewModel.successResponse.observe(viewLifecycleOwner) {
            feedsAdapter!!.notifyItemChanged(it)
            binding.rvFeeds.clearFocus()
        }
        viewModel.followResponse.observe(viewLifecycleOwner) {
            lifecycleScope.launch { viewModel.getFeeds() }
            binding.rvFeeds.clearFocus()
        }
        viewModel.deleteResponse.observe(viewLifecycleOwner) {
            lifecycleScope.launch { viewModel.getFeeds() }
            binding.rvFeeds.clearFocus()
        }
        viewModel.errorNorException.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun setupWidgets() {
        binding.imgProfile.setOnClickListener {
            start<ProfileActivity> {
                putExtra("extra_id", (activity as MainActivity).userAuth.userId)
            }
        }
        binding.edtSearch.setOnClickListener { start<ExploreActivity>() }
        binding.edtWritePost.setOnClickListener {
            start<CreatePostActivity> {
                putExtra(EXTRA_TEXT, EXTRA_TEXT)
                putExtra(EXTRA_SOURCE, PostSource.SOURCE_FEED)
            }
        }
        binding.btnUploadImage.setOnClickListener {
            start<CreatePostActivity> {
                putExtra(EXTRA_IMAGE, EXTRA_IMAGE)
                putExtra(EXTRA_SOURCE, PostSource.SOURCE_FEED)
            }
        }
        binding.btnUploadVideo.setOnClickListener {
            start<CreatePostActivity> {
                putExtra(EXTRA_VIDEO, EXTRA_VIDEO)
                putExtra(EXTRA_SOURCE, PostSource.SOURCE_FEED)
            }
        }
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window!!.attributes.gravity = Gravity.BOTTOM
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(dialogFeedOptionsBinding.root)
        }
        with(dialogFeedOptionsBinding) {
            btnFilter.setOnClickListener {
                dialog.dismiss()
                showBottomSheetDialog()
                binding.rvFeeds.clearFocus()
            }
            btnBackToTop.setOnClickListener {
                dialog.dismiss()
                binding.mainView.fullScroll(ScrollView.FOCUS_UP)
                binding.rvFeeds.clearFocus()
            }
        }
        binding.btnFeedOption.setOnClickListener {
            dialog.show()
            binding.rvFeeds.clearFocus()
        }
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
                binding.rvFeeds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        holder.pausePlayer()
                        super.onScrolled(recyclerView, dx, dy)
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        holder.pausePlayer()
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
                binding.mainView.setOnScrollChangeListener(
                    NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                        holder.pausePlayer()
                    }
                )
            }
        )

        binding.rvFeeds.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = feedsAdapter
        }
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
                    viewModel.deletePost(postId, position)
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

    private fun showBottomSheetDialog() {
        bottomSheetDialog.setContentView(bottomSheetFilterPostBinding.root)
        bottomSheetDialog.show()

        bottomSheetDialog.setOnDismissListener {
            if (!isCanceled) {
                lifecycleScope.launch { viewModel.getFilteredPost(setSelectedCategoryId()) }
            }
            isCanceled = true
        }

        bottomSheetFilterPostBinding.btnApply.setOnClickListener {
            isCanceled = false
            bottomSheetDialog.dismiss()
        }
    }

    private fun setSelectedCategoryId(): Int {
        var selectedCategory = -1
        val checkedButtonName = bottomSheetFilterPostBinding.rgFilterCategory
            .findViewById<RadioButton>(checkedButtonId).text.toString()
        categories.forEach {
            if (it == checkedButtonName) {
                val itIndex = categories.indexOf(it)
                selectedCategory = categoryIds[itIndex]
            }
        }
        return selectedCategory
    }

    override fun onResume() {
        super.onResume()
        if (shouldRefresh) {
            lifecycleScope.launch { viewModel.getFeeds() }
            shouldRefresh = false
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