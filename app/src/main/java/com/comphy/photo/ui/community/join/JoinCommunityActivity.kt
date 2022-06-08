package com.comphy.photo.ui.community.join

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityJoinCommunityBinding
import com.comphy.photo.databinding.BottomSheetFilterCommunityBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.community.join.fragment.JoinCommunityCategoryFragment
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinCommunityActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.jc_tab_category_human_interest,
            R.string.jc_tab_category_portrait,
            R.string.jc_tab_category_landscape,
            R.string.jc_tab_category_fashion,
            R.string.jc_tab_category_journalism
        )
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityJoinCommunityBinding.inflate(layoutInflater)
    }
    private val viewModel: JoinCommunityViewModel by viewModels()
    val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetFilterCommunityBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFilterCommunityBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private var selectedCategoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupObserver()

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnFilter.setOnClickListener { showBottomSheetFilterCommunity() }
        binding.tabCommunityCategory.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                lifecycleScope.launch {
                    when (tab?.text) {
                        getString(R.string.jc_tab_category_human_interest) -> {
                            selectedCategoryId = 7
                            viewModel.getCommunityByCategory(7)
                        }
                        getString(R.string.jc_tab_category_portrait) -> {
                            selectedCategoryId = 8
                            viewModel.getCommunityByCategory(8)
                        }
                        getString(R.string.jc_tab_category_landscape) -> {
                            selectedCategoryId = 13
                            viewModel.getCommunityByCategory(13)
                        }
                        getString(R.string.jc_tab_category_fashion) -> {
                            selectedCategoryId = 16
                            viewModel.getCommunityByCategory(16)
                        }
                        getString(R.string.jc_tab_category_journalism) -> {
                            selectedCategoryId = 5
                            viewModel.getCommunityByCategory(5)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        viewPagerSetupHelper.setupNormal(
            binding.tabCommunityCategory,
            binding.vpTabCommunityCategory,
            pagerAdapter(
                listOf(
                    JoinCommunityCategoryFragment(),
                    JoinCommunityCategoryFragment(),
                    JoinCommunityCategoryFragment(),
                    JoinCommunityCategoryFragment(),
                    JoinCommunityCategoryFragment()
                )
            ),
            TAB_TITLES
        )
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
    }

    private fun showBottomSheetFilterCommunity() {
        with(bottomSheetFilterCommunityBinding) {
            val selectedFilter = rgFilterCommunity
                .findViewById<RadioButton>(rgFilterCommunity.checkedRadioButtonId).text.toString()
            val trimmedSelectedFilter = selectedFilter.trim().lowercase()

            btnApply.setOnClickListener {
                lifecycleScope.launch {
                    when {
                        trimmedSelectedFilter.contains("pub") -> {
                            // FILTER BY PRIVATE PUBLIC NOT READY YET
                            viewModel.getCommunityByCategory(selectedCategoryId)
                        }
                        trimmedSelectedFilter.contains("pri") -> {
                            // FILTER BY PRIVATE PUBLIC NOT READY YET
                            viewModel.getCommunityByCategory(selectedCategoryId)
                        }
                    }
                }
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetFilterCommunityBinding.root)
        bottomSheetDialog.show()
    }
}