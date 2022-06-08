package com.comphy.photo.ui.bookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityBookmarkBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.bookmark.fragment.event.BookmarkEventFragment
import com.comphy.photo.ui.bookmark.fragment.job.BookmarkJobFragment
import com.comphy.photo.ui.bookmark.fragment.post.BookmarkPostFragment
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.explore_tab_event,
            R.string.explore_tab_job,
            R.string.explore_tab_post
        )
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityBookmarkBinding.inflate(layoutInflater)
    }
    val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: BookmarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.successResponse.observe(this) {
            // TODO: REFRESH BOOKMARKS LIST
        }
    }

    private fun setupWidgets() {
        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            viewPagerSetupHelper.setup(
                tabBookmark,
                vpTabBookmark,
                supportFragmentManager,
                pagerAdapter(listOf(
                    BookmarkEventFragment(),
                    BookmarkJobFragment(),
                    BookmarkPostFragment()
                )),
                TAB_TITLES
            )
        }
    }
}