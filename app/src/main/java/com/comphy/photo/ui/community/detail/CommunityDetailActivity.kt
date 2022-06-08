package com.comphy.photo.ui.community.detail

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ActivityCommunityDetailBinding
import com.comphy.photo.databinding.BottomSheetCommunityCodeBinding
import com.comphy.photo.databinding.BottomSheetCommunityMembersBinding
import com.comphy.photo.databinding.BottomSheetShareCommunityBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.community.detail.adapter.CommunityMembersAdapter
import com.comphy.photo.ui.community.detail.fragment.CommunityPhotoFragment
import com.comphy.photo.ui.community.detail.fragment.CommunityPostFragment
import com.comphy.photo.ui.community.edit.EditCommunityActivity
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.resources.colorSL
import splitties.resources.drawable

@AndroidEntryPoint
class CommunityDetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.community_tab_post,
            R.string.community_tab_photo
        )
        private const val EXTRA_DETAIL = "extra_detail"
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
    }

    val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCommunityDetailBinding.inflate(layoutInflater)
    }
    private val viewModel: CommunityDetailViewModel by viewModels()
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetCommunityCodeBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetCommunityCodeBinding.inflate(layoutInflater)
    }
    private val bottomSheetShareCommunityBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetShareCommunityBinding.inflate(layoutInflater)
    }
    private val bottomSheetCommunityMembersBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetCommunityMembersBinding.inflate(layoutInflater)
    }

    private val ownedCommunity = mutableListOf<FollowCommunityResponseContentItem>()
    private var extraDetail: FollowCommunityResponseContentItem? = null
    var contentItem: FollowCommunityResponseContentItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraDetail = intent.getParcelableExtra(EXTRA_DETAIL)
        if (extraDetail == null) finish()

        lifecycleScope.launch {
            viewModel.getCreatedCommunity()
            viewModel.getUserDetails()
            if (isAdmin()) viewModel.getAdminCommunityDetails(extraDetail!!.id)
            else viewModel.getCommunityDetails(extraDetail!!.id)
            viewModel.getCommunityMembers(extraDetail!!.id)
        }
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.createdCommunity.observe(this) { ownedCommunity.addAll(it) }
        viewModel.detailCommunity.observe(this) {
            contentItem = it
            setupWidgets()
        }
        viewModel.onErrorNorException.observe(this) {
            bottomSheetCommunityCodeBinding.errorLayout.visibility = View.VISIBLE
        }
        viewModel.onSuccess.observe(this) {
            if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
        }
        viewModel.communityMembers.observe(this) {
            bottomSheetCommunityMembersBinding.rvCommunityMembers.apply {
                layoutManager = LinearLayoutManager(this@CommunityDetailActivity)
                adapter = CommunityMembersAdapter(it)
            }
        }
    }

    private fun setupWidgets() {
        binding.btnBackToTop.setOnClickListener {
            binding.mainView.fullScroll(ScrollView.FOCUS_UP)
            binding.vpTabCommunity.clearFocus()
        }
        viewPagerSetupHelper.setupNormal(
            binding.tabCommunity,
            binding.vpTabCommunity,
            pagerAdapter(listOf(CommunityPostFragment(), CommunityPhotoFragment())),
            TAB_TITLES
        )
        Glide.with(this)
            .load(extraDetail!!.profilePhotoCommunityLink)
            .placeholder(drawable(R.drawable.ic_placeholder_people))
            .error(drawable(R.drawable.ic_placeholder_people))
            .centerCrop()
            .into(binding.imgProfile)

        Glide.with(this)
            .load(extraDetail!!.bannerPhotoCommunityLink)
            .placeholder(drawable(R.drawable.img_banner_placeholder))
            .error(drawable(R.drawable.img_banner_placeholder))
            .centerCrop()
            .into(binding.imgBanner)

        binding.txtCommunityName.text = extraDetail!!.communityName
        binding.txtCommunityCategory.text = extraDetail!!.categoryCommunity.name
        binding.txtCommunityLocation.text = extraDetail!!.location
        binding.txtCommunityType.text =
            String.format(getString(R.string.placeholder_community_type), extraDetail!!.communityType)
        binding.txtCommunityDescription.text = extraDetail!!.description
        binding.txtCommunityMembersCount.text = String.format(
            getString(R.string.placeholder_community_members_count),
            (extraDetail!!.amountJoinedCommunity.toString().takeIf { it.toInt() < 10 } ?: "10+")
        )

        if (isAdmin()) {
            binding.btnSettings.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    start<EditCommunityActivity> {
                        putExtra(EXTRA_CONTENT_ITEM, contentItem)
                    }
                }
            }
            binding.btnJoinCommunity.visibility = View.GONE
        } else {
            binding.btnSettings.visibility = View.INVISIBLE
            binding.btnJoinCommunity.apply {
                if (contentItem!!.joined) {
                    text = getString(R.string.string_leave_community)
                    backgroundTintList = colorSL(R.color.button_disabled)
                    setTextColor(colorSL(R.color.text_disabled))
                    setOnClickListener {
                        lifecycleScope.launch { viewModel.leaveCommunity(contentItem!!.id) }
                    }
                } else {
                    text = getString(R.string.string_join_community)
                    backgroundTintList = colorSL(R.color.primary_orange)
                    setTextColor(colorSL(R.color.white))
                    setOnClickListener {
                        if (contentItem!!.communityCode != null) setupCommunityCodeBottomSheet()
                        else lifecycleScope.launch { viewModel.joinCommunity(contentItem!!.id) }
                    }
                }
                visibility = View.VISIBLE

            }
        }
        binding.btnShare.setOnClickListener { setupBottomSheetShareCommunity() }
        binding.txtCommunityMembersCount.setOnClickListener { setupBottomSheetCommunityMembers() }
    }

    private fun setupCommunityCodeBottomSheet() {
        var isCancelled = true
        val codeInputWidgets = listOf(
            bottomSheetCommunityCodeBinding.edtCode1,
            bottomSheetCommunityCodeBinding.edtCode2,
            bottomSheetCommunityCodeBinding.edtCode3,
            bottomSheetCommunityCodeBinding.edtCode4
        )
        val codeResult =
            bottomSheetCommunityCodeBinding.edtCode1.text.toString() + bottomSheetCommunityCodeBinding.edtCode2.text.toString() + bottomSheetCommunityCodeBinding.edtCode3.text.toString() + bottomSheetCommunityCodeBinding.edtCode4.text.toString()

        codeInputWidgets.forEach {
            it.isFocusable = true
            it.isClickable = true
            it.isFocusableInTouchMode = true
            it.doAfterTextChanged {
                bottomSheetCommunityCodeBinding.errorLayout.visibility = View.GONE
                isFieldEmpty(codeInputWidgets)
                bottomSheetCommunityCodeBinding.btnSaveChange.apply {
                    isEnabled = !isFieldEmpty(codeInputWidgets)
                    if (isEnabled) setOnClickListener {
                        isCancelled = false
                        lifecycleScope.launch { viewModel.joinCommunity(extraDetail!!.id, codeResult) }
                    }
                }
            }
        }

        bottomSheetDialog.setContentView(bottomSheetCommunityCodeBinding.root)
        bottomSheetDialog.show()

        bottomSheetDialog.setOnDismissListener {
            if (!isCancelled) onResume()
        }
    }

    private fun setupBottomSheetShareCommunity() {
        bottomSheetDialog.setContentView(bottomSheetShareCommunityBinding.root)
        bottomSheetDialog.show()
    }

    private fun setupBottomSheetCommunityMembers() {
        bottomSheetDialog.setContentView(bottomSheetCommunityMembersBinding.root)
        bottomSheetDialog.show()
    }

    private fun isFieldEmpty(fields: List<EditText>): Boolean {
        fields.forEach {
            if (it.text.isEmpty()) return true
        }
        return false
    }

    private fun isAdmin(): Boolean = extraDetail!!.theAdmin
//        ownedCommunity.forEach { return extraDetail!!.id == it.id }
//        return false
}