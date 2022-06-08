package com.comphy.photo.ui.search.result

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivitySearchResultBinding
import com.comphy.photo.databinding.BottomSheetFilterSearchBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.search.result.fragment.community.SearchCommunityFragment
import com.comphy.photo.ui.search.result.fragment.event.SearchEventFragment
import com.comphy.photo.ui.search.result.fragment.job.SearchJobFragment
import com.comphy.photo.ui.search.result.fragment.post.SearchPostFragment
import com.comphy.photo.ui.search.result.fragment.user.SearchUserFragment
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.resources.drawable
import splitties.toast.toast

@AndroidEntryPoint
class SearchResultActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.explore_tab_user,
            R.string.explore_tab_community,
            R.string.explore_tab_job,
            R.string.explore_tab_post,
            R.string.explore_tab_event
        )
        private const val EXTRA_KEY = "extra_key"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySearchResultBinding.inflate(layoutInflater)
    }
    val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetFilterSearchBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFilterSearchBinding.inflate(layoutInflater)
    }
    val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: SearchResultViewModel by viewModels()
    var extraKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraKey = intent.getStringExtra(EXTRA_KEY)

        if (extraKey == null) finish()
        else {
            lifecycleScope.launch {
                viewModel.getUserDetails()
                viewModel.getCities()
            }
            setupObserver()
            setupWidgets()
        }
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.errorNorException.observe(this) { toast(it) }
        viewModel.cities.observe(this) {
            val locationAdapter =
                ArrayAdapter(
                    this,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    Extension.formatCity(it)
                )

            bottomSheetFilterSearchBinding.edtLocation.apply {
                setDropDownBackgroundDrawable(drawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
    }

    private fun setupWidgets() {
        with(binding) {
            edtSearch.apply {
                setText(extraKey!!)
                setOnClickListener { onBackPressed() }
            }
            btnBack.setOnClickListener { onBackPressed() }
            viewPagerSetupHelper.setupNormal(
                tabSearch,
                vpTabSearch,
                pagerAdapter(
                    listOf(
                        SearchUserFragment(),
                        SearchCommunityFragment(),
                        SearchPostFragment(),
                        SearchJobFragment(),
                        SearchEventFragment()
                    )
                ),
                TAB_TITLES
            )
            btnFilter.setOnClickListener { showBottomSheetFilter() }
        }
    }

    private fun showBottomSheetFilter() {
        with(bottomSheetFilterSearchBinding) {
            edtLocation.doAfterTextChanged {
                btnApply.isEnabled = edtLocation.text.isNotEmpty()
            }
            btnApply.setOnClickListener {
                val location = edtLocation.text.toString().split(",")[0]
                lifecycleScope.launch {
                    viewModel.getFilteredUsers(extraKey!!, location)
                    viewModel.getFilteredCommunities(extraKey!!, location)
                    viewModel.getFilteredPost(extraKey!!, location)
                    // TODO: FILTER GET JOBS BY LOCATION
                    // TODO: FILTER GET EVENTS BY LOCATION
                }
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetFilterSearchBinding.root)
        bottomSheetDialog.show()
    }
}