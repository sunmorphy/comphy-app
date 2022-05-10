package com.comphy.photo.ui.community

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
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.utils.Extension.changeColor
import com.comphy.photo.utils.Extension.changeDrawable
import com.comphy.photo.utils.Extension.formatRegency
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
import splitties.toast.toast
import java.io.File

@AndroidEntryPoint
class CreateCommunityActivity : BaseCommunityActivity() {

    private lateinit var binding: ActivityCreateCommunityBinding
    private val viewModel: CreateCommunityViewModel by viewModels()
    private val imagesPath = mutableListOf("", "")
    private val imagesUrl = mutableListOf<String>()
    private var profilePath = ""
    private var bannerPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch { viewModel.getRegencies() }

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

        val communityCategory = resources.getStringArray(R.array.community_category)
        val communityCategoryAdapter = ArrayAdapter(
            this@CreateCommunityActivity,
            R.layout.custom_dropdown_category,
            R.id.txtCategoryItem,
            communityCategory
        )
        binding.edtCommunityCategory.apply {
            setDropDownBackgroundDrawable(this@CreateCommunityActivity.changeDrawable(R.drawable.bg_dialog))
            setAdapter(communityCategoryAdapter)
        }

        requiredWidgets.forEach { it.doAfterTextChanged { setToolbarButtonEnable() } }
        binding.rgCommunityType.setOnCheckedChangeListener { _, _ -> setToolbarButtonEnable() }
    }

    override fun setupClickListener() {
        binding.imgBanner.setOnClickListener {
            requestAccessForFile {
                openPicker {
                    println(File(it.mediaPath).sizeInMb)
                    bannerPath = it.mediaPath
                    imagesPath[0] = it.mediaPath
                    Glide.with(this)
                        .load(imagesPath[0])
                        .centerCrop()
                        .into(binding.imgBanner)
                    binding.txtBannerMax.setTextColor(changeColor(R.color.white))
                    binding.txtBannerSize.setTextColor(changeColor(R.color.white))
                    binding.imgBannerOverlay.visibility = View.VISIBLE
                }
            }
        }
        binding.imgProfile.setOnClickListener {
            requestAccessForFile {
                openPicker {
                    println(File(it.mediaPath).sizeInMb)
                    profilePath = it.mediaPath
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
            lifecycleScope.launch { viewModel.getUploadLink(UploadType.IMAGE, listPath.size) }
        }
    }

    override fun setupObserver() {
        viewModel.uploadsUrl.observe(this) {
            it.forEach { item -> imagesUrl.add(item.storagePath) }
            lifecycleScope.launch {
                uploadImages(it[0].storageUrl, PROFILE)
                uploadImages(it[1].storageUrl, BANNER)
                viewModel.createCommunity(
                    communityName = binding.edtCommunityName.text.toString(),
                    description = binding.edtCommunityDescription.text.toString(),
                    location = binding.edtCommunityLocation.text.toString().split(",")[0],
                    communityType = binding.rgCommunityType
                        .findViewById<RadioButton>(binding.rgCommunityType.checkedRadioButtonId).text.toString(),
                    categoryCommunityId = 3,
                    profilePhotoCommunityLink = imagesPath[1].ifEmpty { null },
                    bannerPhotoCommunityLink = imagesPath[0].ifEmpty { null }
                )
            }
        }
        viewModel.exceptionResponse.observe(this) { if (it != null) toast(it) }
        viewModel.communityResponse.observe(this) {
            start<MainActivity>()
            finishAffinity()
        }
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.regencies.observe(this) { regencies ->
            val locationAdapter =
                ArrayAdapter(
                    this@CreateCommunityActivity,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    formatRegency(regencies)
                )

            binding.edtCommunityLocation.apply {
                setDropDownBackgroundDrawable(this@CreateCommunityActivity.changeDrawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
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
                        toast("File size must be smaller than 3mb")
                    }
                }
            }
    }

    private suspend fun uploadImages(url: String, imageType: CommunityImageType) {
        when (imageType) {
            PROFILE -> {
                val reqFile = File(imagesPath[0]).asRequestBody()
                viewModel.uploadImageNonPost(url, reqFile)
            }
            BANNER -> {
                val reqFile = File(imagesPath[1]).asRequestBody()
                viewModel.uploadImageNonPost(url, reqFile)
            }
        }
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