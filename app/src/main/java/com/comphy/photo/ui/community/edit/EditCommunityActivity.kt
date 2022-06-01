package com.comphy.photo.ui.community.edit

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.base.activity.BasePermissionActivity
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ActivityEditCommunityBinding
import com.comphy.photo.databinding.BottomSheetBinding
import com.comphy.photo.databinding.BottomSheetDeleteCommunityBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.community.edit.fragment.EditCommunityMemberFragment
import com.comphy.photo.ui.community.edit.fragment.EditCommunityProfileFragment
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.utils.Extension.loadAnim
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.resources.color
import splitties.resources.drawable
import splitties.toast.toast

@AndroidEntryPoint
class EditCommunityActivity : BasePermissionActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.ec_tab_profile,
            R.string.ec_tab_member
        )
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityEditCommunityBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) {
        CustomLoading(this)
    }
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) {
        ViewPagerSetupHelper(this)
    }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetBinding.inflate(layoutInflater)
    }
    private val bottomSheetDeleteCommunityBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDeleteCommunityBinding.inflate(layoutInflater)
    }
    private val viewModel: EditCommunityViewModel by viewModels()
    var contentItem: FollowCommunityResponseContentItem? = null
    val imagesPath = mutableListOf("", "")
    var profileUrl : String? = ""
    var bannerUrl : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        contentItem = intent.getParcelableExtra(EXTRA_CONTENT_ITEM)
        if (contentItem == null) finish()

        profileUrl = contentItem!!.profilePhotoCommunityLink
        bannerUrl = contentItem!!.bannerPhotoCommunityLink

        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.isBanLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.isDeleteLoading.observe(this) {
            bottomSheetDeleteCommunityBinding.apply {
                if (it) {
                    btnDelete.visibility = View.INVISIBLE
                    imgLoadingBtn.apply {
                        visibility = View.VISIBLE
                        startAnimation(loadAnim(R.anim.btn_loading_anim))
                    }
                    bottomSheetDialog.setCancelable(false)
                } else {
                    btnDelete.visibility = View.VISIBLE
                    imgLoadingBtn.apply {
                        clearAnimation()
                        visibility = View.GONE
                    }
                    bottomSheetDialog.setCancelable(true)
                }
                listOf(btnCancel, btnDelete).forEach { btn -> btn.isEnabled = !it }
            }
        }
        viewModel.errorNorExceptionResponse.observe(this) { toast(it) }
        viewModel.successResponse.observe(this) {
            if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
            finish()
        }
        viewModel.banResponse.observe(this) {
            val spannedDesc = SpannableString(
                String.format(getString(R.string.ec_community_ban_success_desc), it)
            )
            spannedDesc.apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this@EditCommunityActivity,
                            R.color.neutral_black_30
                        )
                    ),
                    0,
                    (getString(R.string.ec_community_ban_success_desc).length - it.length),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this@EditCommunityActivity,
                            R.color.neutral_black
                        )
                    ),
                    (getString(R.string.ec_community_ban_success_desc).length - it.length),
                    String.format(getString(R.string.ec_community_ban_success_desc), it).length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            bottomSheetBinding.apply {
                animView.setAnimation(R.raw.anim_success)
                txtSheetTitle.text = getString(R.string.string_success_ban)
                txtSheetDesc.text = spannedDesc
                btnSheetAction.apply {
                    text = getString(R.string.string_ok)
                    setOnClickListener { bottomSheetDialog.dismiss() }
                }
            }
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.show()
        }
    }

    private fun setupWidgets() {
        binding.imgBanner.setOnClickListener {
            requestAccessForFile(UwMediaPicker.GalleryMode.ImageGallery) {
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
        binding.imgProfile.setOnClickListener {
            requestAccessForFile(UwMediaPicker.GalleryMode.ImageGallery) {
                imagesPath[1] = it.mediaPath
                Glide.with(this)
                    .load(imagesPath[1])
                    .centerCrop()
                    .into(binding.imgProfile)
            }
        }
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnDeleteCommunity.setOnClickListener { setupBottomSheetDeleteCommunity() }
        Glide.with(this)
            .load(contentItem!!.profilePhotoCommunityLink)
            .placeholder(drawable(R.drawable.ic_placeholder_people))
            .error(drawable(R.drawable.ic_placeholder_people))
            .centerCrop()
            .into(binding.imgProfile)

        if (contentItem!!.bannerPhotoCommunityLink == null) {
            Glide.with(this)
                .load(R.drawable.bg_upload)
                .centerCrop()
                .into(binding.imgBanner)
            binding.txtBannerMax.setTextColor(color(R.color.neutral_black_60))
            binding.txtBannerSize.setTextColor(color(R.color.neutral_black_50))
            binding.imgBannerOverlay.visibility = View.GONE
        } else {
            Glide.with(this)
                .load(contentItem!!.bannerPhotoCommunityLink)
                .placeholder(drawable(R.drawable.img_banner_placeholder))
                .error(drawable(R.drawable.img_banner_placeholder))
                .centerCrop()
                .into(binding.imgBanner)
            binding.txtBannerMax.setTextColor(color(R.color.white))
            binding.txtBannerSize.setTextColor(color(R.color.white))
            binding.imgBannerOverlay.visibility = View.VISIBLE
        }

        viewPagerSetupHelper.setup(
            binding.tabEditCommunity,
            binding.vpTabEditCommunity,
            supportFragmentManager,
            pagerAdapter(listOf(EditCommunityProfileFragment(), EditCommunityMemberFragment())),
            TAB_TITLES
        )
    }

    private fun setupBottomSheetDeleteCommunity() {
        bottomSheetDeleteCommunityBinding.btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDeleteCommunityBinding.btnDelete.setOnClickListener {
            lifecycleScope.launch { viewModel.deleteCommunity(contentItem!!.id) }
        }

        bottomSheetDialog.setContentView(bottomSheetDeleteCommunityBinding.root)
        bottomSheetDialog.show()
    }
}