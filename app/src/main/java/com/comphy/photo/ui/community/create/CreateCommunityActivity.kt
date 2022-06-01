package com.comphy.photo.ui.community.create

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaModel
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseCommunityActivity
import com.comphy.photo.databinding.ActivityCreateCommunityBinding
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.utils.Extension.changeDrawable
import com.comphy.photo.utils.Extension.formatCity
import com.comphy.photo.utils.Extension.sizeInMb
import com.comphy.photo.vo.CommunityImageType
import com.comphy.photo.vo.CommunityImageType.BANNER
import com.comphy.photo.vo.CommunityImageType.PROFILE
import com.comphy.photo.vo.MediaSize
import com.comphy.photo.vo.UploadType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.asRequestBody
import splitties.activities.start
import splitties.resources.color
import splitties.resources.drawable
import splitties.toast.toast
import java.io.File

@AndroidEntryPoint
class CreateCommunityActivity : BaseCommunityActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCreateCommunityBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: CreateCommunityViewModel by viewModels()
    private val categories = mutableListOf<String>()
    private val categoryIds = mutableListOf<Int>()
    private val imagesPath = mutableListOf("", "")
    private var profileUrl = ""
    private var bannerUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getCities()
            viewModel.getCommunityCategories()
        }

        init()
        setupClickListener()
    }

    override fun init() {
        inputWidgets = listOf(
            binding.edtCommunityCategory,
            binding.edtCommunityName,
            binding.edtCommunityLocation,
            binding.edtCommunityDescription
        )
        requiredWidgets = inputWidgets
        actionWidgets = listOf(binding.btnBack, binding.btnCreateCommunity, binding.btnSave)
        errorWidgets = listOf(
            binding.txtErrorCommunityCategory,
            binding.txtErrorCommunityName,
            binding.txtErrorCommunityLocation,
            binding.txtErrorCommunityDescription
        )
        toolbarLoadingImage = binding.imgLoadingToolbarBtn
        loadingImage = binding.imgLoadingBtn
        responseLayout = binding.errorLayout
        toolbarButtonText = R.string.string_create_community
        mainButtonText = R.string.string_save

        requiredWidgets.forEach { it.doAfterTextChanged { setToolbarButtonEnable() } }
        binding.rgCommunityType.setOnCheckedChangeListener { _, _ -> setToolbarButtonEnable() }
    }

    override fun setupClickListener() {
        binding.imgBanner.setOnClickListener {
            requestAccessForFile {
                openPicker {
                    imagesPath[0] = it.mediaPath
                    Glide.with(this)
                        .load(imagesPath[0])
                        .centerCrop()
                        .into(binding.imgBanner)
                    binding.txtBannerMax.setTextColor(color(R.color.white))
                    binding.txtBannerSize.setTextColor(color(R.color.white))
                    binding.imgBannerOverlay.visibility = View.VISIBLE
                }
            }
        }
        binding.imgProfile.setOnClickListener {
            requestAccessForFile {
                openPicker {
                    imagesPath[1] = it.mediaPath
                    Glide.with(this)
                        .load(imagesPath[1])
                        .centerCrop()
                        .into(binding.imgProfile)
                }
            }
        }
        binding.btnSave.setOnClickListener {
            isMainButton = true
            setRequiredFieldError(false)
            setRadioError(binding.rgCommunityType.checkedRadioButtonId == -1)

            val listPath = mutableListOf<String>()
            imagesPath.forEach { path -> if (path.isNotEmpty()) listPath.add(path) }

            if (isRequiredFieldEmpty()) {
                setRequiredFieldError(true, eachField = true)

            } else {
                lifecycleScope.launch { viewModel.getUploadLink(UploadType.IMAGE, listPath.size) }
            }
        }
        binding.btnCreateCommunity.setOnClickListener {
            isMainButton = false
            val listPath = mutableListOf<String>()
            imagesPath.forEach { path -> if (path.isNotEmpty()) listPath.add(path) }
            lifecycleScope.launch {
                if (listPath.isEmpty()) createCommunity()
                else viewModel.getUploadLink(UploadType.IMAGE, listPath.size)
            }
        }
    }

    override fun setupObserver() {
        viewModel.isFetching.observe(this) {
            if (it) customLoading.show() else customLoading.dismiss()
        }
        viewModel.uploadsUrl.observe(this) {
            lifecycleScope.launch {
                uploadImages(it[0].storageUrl, PROFILE)
                uploadImages(it[1].storageUrl, BANNER)
                profileUrl = it[0].storagePath
                bannerUrl = it[1].storagePath
                createCommunity()
            }
        }
        viewModel.exceptionResponse.observe(this) { if (it != null) toast(it) }
        viewModel.communityResponse.observe(this) {
            start<MainActivity>()
            finishAffinity()
        }
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.cities.observe(this) {
            val locationAdapter =
                ArrayAdapter(
                    this@CreateCommunityActivity,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    formatCity(it)
                )

            binding.edtCommunityLocation.apply {
                setDropDownBackgroundDrawable(this@CreateCommunityActivity.changeDrawable(R.drawable.bg_dialog))
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
                this@CreateCommunityActivity,
                R.layout.custom_dropdown_category,
                R.id.txtCategoryItem,
                categories
            )
            binding.edtCommunityCategory.apply {
                setDropDownBackgroundDrawable(drawable(R.drawable.bg_dialog))
                setAdapter(communityCategoryAdapter)
            }
        }
    }

    private fun openPicker(selectedMedia: (UwMediaPickerMediaModel) -> Unit) {
        UwMediaPicker.with(this)
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery)
            .setGridColumnCount(2)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .launch {
                it?.forEach { item ->
                    val file = File(item.mediaPath)
                    if (file.sizeInMb <= MediaSize.IMAGE_NON_POST) {
                        selectedMedia(item)
                    } else {
                        toast("File harus lebih kecil dari 3MB")
                    }
                }
            }
    }

    private suspend fun uploadImages(url: String, imageType: CommunityImageType) {
        when (imageType) {
            PROFILE -> {
                val reqFile = File(imagesPath[1]).asRequestBody()
                viewModel.uploadImageNonPost(url, reqFile)
            }
            BANNER -> {
                val reqFile = File(imagesPath[0]).asRequestBody()
                viewModel.uploadImageNonPost(url, reqFile)
            }
        }
    }

    private fun setSelectedCategory(): Int {
        var selectedCategory = -1
        categories.forEach {
            if (it == binding.edtCommunityCategory.text.toString()) {
                val itIndex = categories.indexOf(it)
                selectedCategory = categoryIds[itIndex]
            }
        }
        return selectedCategory
    }

    private suspend fun createCommunity() {
        viewModel.createCommunity(
            communityName = binding.edtCommunityName.text.toString(),
            description = binding.edtCommunityDescription.text.toString(),
            location = binding.edtCommunityLocation.text.toString().split(",")[0],
            communityType = binding.rgCommunityType
                .findViewById<RadioButton>(binding.rgCommunityType.checkedRadioButtonId).text.toString(),
            categoryCommunityId = setSelectedCategory().takeIf { v -> v != -1 } ?: 5,
            profilePhotoCommunityLink = profileUrl.ifEmpty { null },
            bannerPhotoCommunityLink = bannerUrl.ifEmpty { null }
        )
    }

    private fun setToolbarButtonEnable() {
        binding.btnCreateCommunity.isEnabled =
            (!isRequiredFieldEmpty() && binding.rgCommunityType.checkedRadioButtonId != -1)
    }

    private fun setRadioError(state: Boolean) {
        if (state) {
            binding.txtErrorCommunityType.visibility = View.VISIBLE
        } else {
            binding.txtErrorCommunityType.visibility = View.GONE
        }
    }
}