package com.comphy.photo.ui.profile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.databinding.ActivityProfileBinding
import com.comphy.photo.databinding.BottomSheetFollowBinding
import com.comphy.photo.databinding.BottomSheetPrivacySecurityBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.profile.fragment.ProfileAboutFragment
import com.comphy.photo.ui.profile.fragment.ProfilePostFragment
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.resources.color
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.profile_tab_post,
            R.string.profile_tab_about
        )
        private const val EXTRA_ID = "extra_id"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetFollowBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFollowBinding.inflate(layoutInflater)
    }
    private val bottomSheetPrivacySecurityBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetPrivacySecurityBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var userAuth: UserAuth

    var extraId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraId = intent.getIntExtra(EXTRA_ID, -1)
        setupWidgets()

        lifecycleScope.launch {
            viewModel.getUserDetails()
            viewModel.getUserFollowing()
            viewModel.getUserFollowers()
        }

        viewPagerSetupHelper.setup(
            binding.tabProfile,
            binding.vpTabProfile,
            supportFragmentManager,
            pagerAdapter(listOf(ProfilePostFragment(), ProfileAboutFragment())),
            TAB_TITLES
        )

        setupObserver()
    }

    private fun setupWidgets() {
        if (extraId != -1) {
            if (extraId == userAuth.userId) {
                binding.btnLogout.visibility = View.VISIBLE
                binding.btnAddUser.visibility = View.GONE
                binding.btnSettings.visibility = View.VISIBLE
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnToBookmark.visibility = View.VISIBLE
            } else {
                binding.btnLogout.visibility = View.GONE
                binding.btnAddUser.visibility = View.VISIBLE
                binding.btnSettings.visibility = View.INVISIBLE
                binding.btnEdit.visibility = View.INVISIBLE
                binding.btnToBookmark.visibility = View.GONE
            }
        } else {
            finish()
        }
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.userData.observe(this) {
            when {
                it.profileUrl != null -> {
                    Glide.with(this)
                        .load(it.profileUrl)
                        .centerCrop()
                        .into(binding.imgProfile)
                }
                it.bannerUrl != null -> {
                    Glide.with(this)
                        .load(it.bannerUrl)
                        .centerCrop()
                        .into(binding.imgBanner)
                    binding.txtBannerMax.setTextColor(color(R.color.white))
                    binding.txtBannerSize.setTextColor(color(R.color.white))
                    binding.imgBannerOverlay.visibility = View.VISIBLE
                }
            }
            binding.txtUserName.text = it.fullname
            binding.txtUserJob.text = it.job
            binding.txtUserLocation.text = it.location
            binding.txtFollowerCount.text = it.lengthFollowers.toString()
            binding.txtFollowingCount.text = it.lengthFollowing.toString()
        }
    }
}