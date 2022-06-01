package com.comphy.photo.ui.community.join

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityJoinCommunityBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.community.join.fragment.JoinCommunityCategoryFragment
import com.comphy.photo.utils.Extension.pagerAdapter
import dagger.hilt.android.AndroidEntryPoint

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
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) {
        ViewPagerSetupHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { onBackPressed() }

        viewPagerSetupHelper.setup(
            binding.tabCommunityCategory,
            binding.vpTabCommunityCategory,
            supportFragmentManager,
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
}