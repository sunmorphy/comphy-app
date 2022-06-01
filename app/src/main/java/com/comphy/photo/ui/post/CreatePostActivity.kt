package com.comphy.photo.ui.post

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.comphy.photo.R
import com.comphy.photo.base.activity.BasePostActivity
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ActivityCreatePostBinding
import com.comphy.photo.databinding.BottomSheetBinding
import com.comphy.photo.databinding.BottomSheetChooseCommunityBinding
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.changeDrawable
import com.comphy.photo.utils.Extension.isItsWidthBiggerThanHeight
import com.comphy.photo.utils.Extension.sizeInMb
import com.comphy.photo.vo.MediaSize
import com.comphy.photo.vo.OrientationType
import com.comphy.photo.vo.PostType
import com.comphy.photo.vo.UploadType
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.asRequestBody
import splitties.dimensions.dp
import splitties.resources.color
import splitties.resources.drawable
import splitties.toast.toast
import java.io.File

@AndroidEntryPoint
class CreatePostActivity : BasePostActivity() {

    companion object {
        private const val EXTRA_TEXT = "extra_text"
        private const val EXTRA_IMAGE = "extra_image"
        private const val EXTRA_VIDEO = "extra_video"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCreatePostBinding.inflate(layoutInflater)
    }
    private val sheetCreatePostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetBehavior.from(binding.layoutSheetCreatePost.root)
    }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetChooseCommunityBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetChooseCommunityBinding.inflate(layoutInflater)
    }
    private val bottomSheetBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: CreatePostViewModel by viewModels()
    private var extraText: String? = null
    private var extraImage: String? = null
    private var extraVideo: String? = null
    private var player: ExoPlayer? = null

    private val availableCommunity = mutableListOf<FollowCommunityResponseContentItem>()
    private val categories = mutableListOf<String>()
    private val categoryIds = mutableListOf<Int>()

    private var selectedMediaPath = ""
    private var selectedMediaOrientation = -1
    private var observableMediaPath = MutableLiveData<String>()
    private var isFit = false
    private var selectedPostTo = ""
    private var selectedCommunityName = ""
    private var selectedCommunityId = -1
    private var selectedCategory = -1
    private var isRadioChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getCities()
            viewModel.getCommunityCategories()
            viewModel.getCreatedCommunities()
            viewModel.getJoinedCommunities()
        }

        extraText = intent.getStringExtra(EXTRA_TEXT)
        extraImage = intent.getStringExtra(EXTRA_IMAGE)
        extraVideo = intent.getStringExtra(EXTRA_VIDEO)

        setCreatePostType()
    }

    override fun init() {
        inputWidgets = listOf(
            binding.layoutSheetCreatePost.edtPostTitle,
            binding.layoutSheetCreatePost.edtPostCamera,
            binding.layoutSheetCreatePost.edtPostIso,
            binding.layoutSheetCreatePost.edtPostLens,
            binding.layoutSheetCreatePost.edtPostShutter,
            binding.layoutSheetCreatePost.edtPostFlash,
            binding.layoutSheetCreatePost.edtPostAperture,
            binding.layoutSheetCreatePost.edtLocation,
            binding.layoutSheetCreatePost.edtCommunityCategory
        )
        actionWidgets = listOf(
            binding.btnBack,
            binding.btnUploadPost
        )
        loadingImage = binding.imgLoadingBtn
        buttonText = R.string.string_upload_post
        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = getString(R.string.post_success_title)
            txtSheetDesc.text = getString(R.string.post_success_description)
        }
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    override fun setupObserver() {
        viewModel.isFetching.observe(this) { if (it) customLoading.show() else customLoading.dismiss() }
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.exceptionResponse.observe(this) { if (it != null) toast(it) }
        viewModel.cities.observe(this) {
            val locationAdapter =
                ArrayAdapter(
                    this@CreatePostActivity,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    Extension.formatCity(it)
                )

            binding.layoutSheetCreatePost.edtLocation.apply {
                setDropDownBackgroundDrawable(drawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
        viewModel.categories.observe(this) {
            it.forEach { category ->
                categories.add(category.name)
                categoryIds.add(category.id)
            }
            val communityCategoryAdapter = ArrayAdapter(
                this@CreatePostActivity,
                R.layout.custom_dropdown_category,
                R.id.txtCategoryItem,
                categories
            )
            binding.layoutSheetCreatePost.edtCommunityCategory.apply {
                setDropDownBackgroundDrawable(this@CreatePostActivity.changeDrawable(R.drawable.bg_dialog))
                setAdapter(communityCategoryAdapter)
            }
        }
        viewModel.userCreatedCommunity.observe(this) { availableCommunity.addAll(it) }
        viewModel.userJoinedCommunity.observe(this) { availableCommunity.addAll(it) }
        viewModel.uploadsUrl.observe(this) {
            val reqFile = File(selectedMediaPath).asRequestBody()
            lifecycleScope.launch {
                when {
                    extraImage != null -> {
                        viewModel.uploadImagePost(it[0].storageUrl, reqFile)
                        viewModel.createPost(
                            title = binding.layoutSheetCreatePost.edtPostTitle.text.toString(),
                            camera = binding.layoutSheetCreatePost.edtPostCamera.text.toString()
                                .ifEmpty { null },
                            iso = binding.layoutSheetCreatePost.edtPostIso.text.toString()
                                .ifEmpty { null },
                            lens = binding.layoutSheetCreatePost.edtPostLens.text.toString()
                                .ifEmpty { null },
                            shutterSpeed = binding.layoutSheetCreatePost.edtPostShutter.text.toString()
                                .ifEmpty { null },
                            flash = binding.layoutSheetCreatePost.edtPostFlash.text.toString()
                                .ifEmpty { null },
                            aperture = binding.layoutSheetCreatePost.edtPostAperture.text.toString()
                                .ifEmpty { null },
                            location = binding.layoutSheetCreatePost.edtLocation.text.toString()
                                .split(",")[0]
                                .ifEmpty { null },
                            categoryCommunityId = selectedCategory,
                            communityId = selectedCommunityId.takeIf { value -> value != -1 },
                            linkPhoto = it[0].storagePath,
                            orientationType = selectedMediaOrientation.takeIf { value -> value != -1 }
                                ?: 0
                        )
                    }
                    extraVideo != null -> {
                        viewModel.uploadVideoPost(it[0].storageUrl, reqFile)
                        viewModel.createPost(
                            title = binding.layoutSheetCreatePost.edtPostTitle.text.toString(),
                            description = binding.layoutSheetCreatePost.edtPostCaption.text.toString(),
                            location = binding.layoutSheetCreatePost.edtLocation.text.toString()
                                .split(",")[0]
                                .ifEmpty { null },
                            categoryCommunityId = selectedCategory,
                            communityId = selectedCommunityId.takeIf { value -> value != -1 },
                            linkVideo = it[0].storagePath,
                            orientationType = selectedMediaOrientation.takeIf { value -> value != -1 }
                                ?: 0
                        )
                    }
                    else -> finish()
                }
            }
        }
        viewModel.successResponse.observe(this) {
            toast(it)
            val nextScreenTimer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val nextScreen = "Feed Post (${millisUntilFinished / 1000}s)"
                    bottomSheetBinding.btnSheetAction.apply {
                        setBackgroundColor(color(R.color.primary_green))
                        text = nextScreen
                        setOnClickListener {
                            cancel()
                            onFinish()
                        }
                    }
                }

                override fun onFinish() = finish()
            }

            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.show()
            nextScreenTimer.start()
            bottomSheetDialog.setOnDismissListener { nextScreenTimer.onFinish() }
        }
    }

    private fun setCreatePostType() {
        when {
            extraText != null -> {
                sheetCreatePostBinding.peekHeight = dp(500)
                binding.layoutUpload.visibility = View.GONE
                requiredWidgets = listOf(
                    binding.layoutSheetCreatePost.edtPostTitle,
                    binding.layoutSheetCreatePost.edtPostCaption,
                    binding.layoutSheetCreatePost.edtLocation,
                    binding.layoutSheetCreatePost.edtCommunityCategory
                )
                observeChange()
            }
            extraImage != null -> {
                binding.playerPreview.visibility = View.GONE
                binding.layoutSheetCreatePost.layoutCaption.visibility = View.INVISIBLE
                binding.layoutSheetCreatePost.layoutDataExif.visibility = View.VISIBLE
                requiredWidgets = listOf(
                    binding.layoutSheetCreatePost.edtPostTitle,
                    binding.layoutSheetCreatePost.edtCommunityCategory
                )
                observeChange()
                setUploadAction(
                    changeDrawable(R.drawable.ic_photograph),
                    getString(R.string.post_choose_photo),
                    getString(R.string.post_choose_photo_desc)
                ) {
                    requestAccessForFile {
                        openPicker(
                            UwMediaPicker.GalleryMode.ImageGallery,
                            MediaSize.IMAGE_POST
                        ) { media, file ->
                            selectedMediaPath = media.mediaPath
                            observableMediaPath.value = selectedMediaPath
                            observeMediaOrientation(file.isItsWidthBiggerThanHeight(PostType.IMAGE))

                            Glide.with(this@CreatePostActivity)
                                .load(media.mediaPath)
                                .apply(RequestOptions())
                                .centerCrop()
                                .into(binding.imgPreview)

                            binding.btnExpand.setOnClickListener {
                                if (isFit) {
                                    Glide.with(this@CreatePostActivity)
                                        .load(media.mediaPath)
                                        .apply(RequestOptions())
                                        .centerCrop()
                                        .into(binding.imgPreview)
                                } else {
                                    Glide.with(this@CreatePostActivity)
                                        .load(media.mediaPath)
                                        .apply(RequestOptions())
                                        .fitCenter()
                                        .into(binding.imgPreview)
                                }
                                isFit = !isFit
                                observeMediaOrientation(file.isItsWidthBiggerThanHeight(PostType.IMAGE))
                            }

                        }
                    }
                }
            }
            extraVideo != null -> {
                binding.imgPreview.visibility = View.GONE
                binding.layoutSheetCreatePost.layoutCaption.visibility = View.VISIBLE
                binding.layoutSheetCreatePost.layoutDataExif.visibility = View.INVISIBLE
                requiredWidgets = listOf(
                    binding.layoutSheetCreatePost.edtPostTitle,
                    binding.layoutSheetCreatePost.edtPostCaption,
                    binding.layoutSheetCreatePost.edtCommunityCategory
                )
                observeChange()
                setUploadAction(
                    changeDrawable(R.drawable.ic_video),
                    getString(R.string.post_choose_video),
                    getString(R.string.post_choose_video_desc)
                ) {
                    requestAccessForFile {
                        openPicker(
                            UwMediaPicker.GalleryMode.VideoGallery,
                            MediaSize.VIDEO
                        ) { media, file ->
                            selectedMediaPath = media.mediaPath
                            observableMediaPath.value = selectedMediaPath
                            observeMediaOrientation(file.isItsWidthBiggerThanHeight(PostType.VIDEO))

                            player = ExoPlayer.Builder(this@CreatePostActivity)
                                .build()
                                .also { exoPlayer ->
                                    binding.playerPreview.apply {
                                        visibility = View.VISIBLE
                                        player = exoPlayer
                                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                                    }
                                    val mediaItem = MediaItem.fromUri(Uri.parse(media.mediaPath))
                                    exoPlayer.setMediaItem(mediaItem)
                                    exoPlayer.prepare()

                                    binding.btnExpand.setOnClickListener {
                                        if (isFit) {
                                            binding.playerPreview.apply {
                                                visibility = View.VISIBLE
                                                player = exoPlayer
                                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                                            }
                                            exoPlayer.setMediaItem(mediaItem)
                                            exoPlayer.prepare()
                                        } else {
                                            binding.playerPreview.apply {
                                                visibility = View.VISIBLE
                                                player = exoPlayer
                                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                            }
                                            exoPlayer.setMediaItem(mediaItem)
                                            exoPlayer.prepare()
                                        }
                                        isFit = !isFit
                                        observeMediaOrientation(
                                            file.isItsWidthBiggerThanHeight(
                                                PostType.VIDEO
                                            )
                                        )
                                    }
                                }
                        }
                    }
                }
            }
            else -> finish()
        }
    }

    private fun setUploadAction(
        icon: Drawable?,
        title: String,
        desc: String,
        selectMedia: () -> Unit?
    ) {
        binding.imgUploadIcon.setImageDrawable(icon)
        binding.txtUploadTitle.text = title
        binding.txtUploadDesc.text = desc
        setUploadEnable(true, selectMedia)

        sheetCreatePostBinding.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                setUploadEnable(newState != BottomSheetBehavior.STATE_EXPANDED, selectMedia)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })
    }

    private fun openPicker(
        mode: UwMediaPicker.GalleryMode,
        maxSize: Double,
        selectedMedia: (UwMediaPickerMediaModel, File) -> Unit
    ) {
        UwMediaPicker.with(this)
            .setGalleryMode(mode)
            .setGridColumnCount(2)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .launch {
                it?.forEach { item ->
                    val file = File(item.mediaPath)
                    if (file.sizeInMb <= maxSize) {
                        binding.layoutUploadHint.visibility = View.GONE
                        selectedMedia(item, file)
                    } else {
                        toast("Batas ukuran file adalah ${maxSize.toInt()}MB")
                    }
                }
            }
    }

    private fun observeChange() {
        observableMediaPath.observe(this) { setButtonEnable() }
        requiredWidgets.forEach { edt ->
            edt.doAfterTextChanged {
                setButtonEnable()
                categories.forEach {
                    if (it == binding.layoutSheetCreatePost.edtCommunityCategory.text.toString()) {
                        val itIndex = categories.indexOf(it)
                        selectedCategory = categoryIds[itIndex]
                    }
                }
            }
        }
        binding.layoutSheetCreatePost.rgSetPostTo.setOnCheckedChangeListener { _, _ ->
            isRadioChecked = true
            selectedPostTo =
                binding.layoutSheetCreatePost.rgSetPostTo.findViewById<RadioButton>(
                    binding.layoutSheetCreatePost.rgSetPostTo.checkedRadioButtonId
                ).text.toString()
            setButtonEnable()
        }
        binding.layoutSheetCreatePost.layoutChosenCommunity.setOnClickListener {
            bottomSheetChooseCommunityBinding.btnBack.setOnClickListener { bottomSheetDialog.dismiss() }
            bottomSheetChooseCommunityBinding.rvChooseCommunity.apply {
                layoutManager = LinearLayoutManager(this@CreatePostActivity)
                adapter = CreatePostAdapter(availableCommunity) { name, id, categoryId ->
                    selectedCommunityName = name
                    selectedCommunityId = id
                    selectedCategory = categoryId
                    categoryIds.forEach {
                        if (it == categoryId) {
                            binding.layoutSheetCreatePost.edtCommunityCategory.setText(
                                categories[categoryIds.indexOf(
                                    it
                                )]
                            )
                            binding.layoutSheetCreatePost.edtCommunityCategory.isEnabled = false
                        }
                    }

                    bottomSheetDialog.dismiss()
                }
            }
            bottomSheetDialog.setContentView(bottomSheetChooseCommunityBinding.root)
            bottomSheetDialog.show()
            bottomSheetDialog.setOnDismissListener {
                if (selectedCommunityName.isNotEmpty() && selectedCommunityId != -1) {
                    setChosenCommunity(false)
                    setButtonEnable()
                }
            }
        }

        binding.btnUploadPost.setOnClickListener {
            lifecycleScope.launch {
                when {
                    extraText != null -> {
                        viewModel.createPost(
                            title = binding.layoutSheetCreatePost.edtPostTitle.text.toString(),
                            description = binding.layoutSheetCreatePost.edtPostCaption.text.toString(),
                            location = binding.layoutSheetCreatePost.edtLocation.text.toString()
                                .split(",")[0]
                                .ifEmpty { null },
                            categoryCommunityId = selectedCategory,
                            communityId = selectedCommunityId.takeIf { it != -1 }
                        )
                    }
                    extraImage != null -> {
                        viewModel.getUploadLink(UploadType.IMAGE, 1)
                    }
                    extraVideo != null -> {
                        viewModel.getUploadLink(UploadType.VIDEO, 1)
                    }
                }
            }
        }
    }

    private fun setButtonEnable() {
        if (selectedPostTo.lowercase() == "komunitas") {
            binding.layoutSheetCreatePost.layoutChosenCommunity.visibility =
                View.VISIBLE
            if (extraText != null) {
                binding.btnUploadPost.isEnabled =
                    (!isRequiredFieldEmpty() && isRadioChecked && selectedCommunityId != -1)
            } else {
                binding.btnUploadPost.isEnabled =
                    (!isRequiredFieldEmpty() && isRadioChecked && selectedCommunityId != -1 && selectedMediaPath.isNotEmpty())
            }
        } else {
            setChosenCommunity(true)
            binding.layoutSheetCreatePost.layoutChosenCommunity.visibility =
                View.INVISIBLE
            if (extraText != null) {
                binding.btnUploadPost.isEnabled = (!isRequiredFieldEmpty() && isRadioChecked)
            } else {
                binding.btnUploadPost.isEnabled =
                    (!isRequiredFieldEmpty() && isRadioChecked && selectedMediaPath.isNotEmpty())
            }
        }
    }

    private fun setChosenCommunity(isEmpty: Boolean) {
        if (isEmpty) {
            selectedCommunityName = ""
            selectedCommunityId = -1
            binding.layoutSheetCreatePost.layoutChosenCommunity.background =
                drawable(R.drawable.state_field)
            binding.layoutSheetCreatePost.txtSetChooseCommunity.setTextColor(
                color(R.color.neutral_black)
            )
            ImageViewCompat.setImageTintList(
                binding.layoutSheetCreatePost.btnGo,
                ColorStateList.valueOf(color(R.color.neutral_black))
            )
            binding.layoutSheetCreatePost.txtSetChosenCommunity.apply {
                text = getString(R.string.set_post_to_choose_community_none)
                setTextColor(color(R.color.neutral_black_20))
            }
            if (!binding.layoutSheetCreatePost.edtCommunityCategory.isEnabled) {
                binding.layoutSheetCreatePost.edtCommunityCategory.isEnabled = true
                binding.layoutSheetCreatePost.edtCommunityCategory.text = null
            }
        } else {
            binding.layoutSheetCreatePost.layoutChosenCommunity.background =
                drawable(R.drawable.bg_choose_active)
            binding.layoutSheetCreatePost.txtSetChooseCommunity.setTextColor(
                color(R.color.primary_orange)
            )
            ImageViewCompat.setImageTintList(
                binding.layoutSheetCreatePost.btnGo,
                ColorStateList.valueOf(color(R.color.primary_orange))
            )
            binding.layoutSheetCreatePost.txtSetChosenCommunity.apply {
                text = selectedCommunityName
                setTextColor(color(R.color.primary_orange))
            }
        }
    }

    private fun setUploadEnable(state: Boolean, onSelectMedia: () -> Unit?) {
        if (state) {
            binding.layoutUpload.isEnabled = true
            binding.layoutUpload.setOnClickListener { onSelectMedia() }
        } else {
            binding.layoutUpload.isEnabled = false
        }
    }

    private fun showBottomSheetDialog() {
        val nextScreenTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val nextScreen = "Feed Post (${millisUntilFinished / 1000}s)"
                bottomSheetBinding.btnSheetAction.apply {
                    setBackgroundColor(color(R.color.primary_green))
                    text = nextScreen
                    setOnClickListener {
                        cancel()
                        onFinish()
                    }
                }
            }

            override fun onFinish() = finish()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
        nextScreenTimer.start()
        bottomSheetDialog.setOnDismissListener { nextScreenTimer.onFinish() }
    }

    private fun observeMediaOrientation(fitOrientation: Int) {
        selectedMediaOrientation = (if (!isFit) OrientationType.SQUARE else fitOrientation)
    }

    override fun onDestroy() {
        availableCommunity.clear()
        super.onDestroy()
    }
}