package com.comphy.photo.ui.main.fragment.feed

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.ScrollView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.BottomSheetFilterPostBinding
import com.comphy.photo.databinding.FragmentFeedBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.ui.main.MainViewModel
import com.comphy.photo.ui.main.fragment.feed.fragment.FeedMainFragment
import com.comphy.photo.ui.main.fragment.feed.fragment.FeedRecommendationFragment
import com.comphy.photo.ui.post.CreatePostActivity
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.search.explore.main.ExploreActivity
import com.comphy.photo.utils.Extension.formatLocationInput
import com.comphy.photo.utils.Extension.loadAnim
import com.comphy.photo.utils.Extension.pagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import splitties.dimensions.dp
import splitties.fragments.start
import splitties.resources.color
import splitties.resources.colorSL
import splitties.resources.drawable

class FeedFragment : Fragment() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.string_feed_main,
            R.string.string_feed_recommendation
        )
    }

    private var _binding: FragmentFeedBinding? = null
    val binding get() = _binding!!

    private val viewModel: FeedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(requireContext()) }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetFilterPostBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFilterPostBinding.inflate(layoutInflater)
    }
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) {
        ViewPagerSetupHelper(requireContext())
    }
    private val categories = mutableListOf<String>()
    private val categoryIds = mutableListOf<Int>()
    private var isCanceled = true
    private var checkedButtonId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getCommunityCategories() }
        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(viewLifecycleOwner) { customLoading.showLoading(it) }
        mainViewModel.userData.observe(viewLifecycleOwner) {
            binding.txtLocation.text = formatLocationInput(it.location!!)
            Glide.with(this)
                .load(it.profileUrl)
                .placeholder(activity?.drawable(R.drawable.ic_placeholder_people))
                .error(activity?.drawable(R.drawable.ic_placeholder_people))
                .centerCrop()
                .into(binding.imgProfile)
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            it.forEach { category ->
                categories.add(category.name)
                categoryIds.add(category.id)

                val rbCategory = RadioButton(requireContext())
                rbCategory.id = View.generateViewId()
                rbCategory.text = category.name
                rbCategory.setTextColor(color(R.color.radio_text))
                rbCategory.setPaddingRelative(
                    requireContext().dp(23),
                    requireContext().dp(8),
                    requireContext().dp(23),
                    requireContext().dp(8)
                )
                rbCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                rbCategory.typeface =
                    ResourcesCompat.getFont(requireContext(), R.font.poppins_medium)
                rbCategory.buttonDrawable = drawable(R.drawable.state_radio)
                rbCategory.buttonTintList = colorSL(R.color.primary_orange)
                if (category.name.lowercase().contains("semua")) rbCategory.isChecked = true
                bottomSheetFilterPostBinding.rgFilterCategory.addView(rbCategory)
            }
            checkedButtonId = bottomSheetFilterPostBinding.rgFilterCategory.checkedRadioButtonId
        }
    }

    private fun setupWidgets() {
        binding.imgProfile.setOnClickListener {
            start<ProfileActivity> {
                putExtra("extra_id", (activity as MainActivity).userAuth.userId)
            }
        }
        binding.edtSearch.setOnClickListener { start<ExploreActivity>() }
        binding.edtWritePost.setOnClickListener {
            start<CreatePostActivity> { putExtra("extra_text", "extra_text") }
        }
        binding.btnUploadImage.setOnClickListener {
            start<CreatePostActivity> { putExtra("extra_image", "extra_image") }
        }
        binding.btnUploadVideo.setOnClickListener {
            start<CreatePostActivity> { putExtra("extra_video", "extra_video") }
        }
        binding.btnBackToTop.setOnClickListener { binding.mainView.fullScroll(ScrollView.FOCUS_UP) }
        binding.btnFilter.setOnClickListener {
            binding.btnFilter.startAnimation(activity?.loadAnim(R.anim.btn_filter_anim))
            showBottomSheetDialog()
        }
        viewPagerSetupHelper.setupFeed(
            binding.tabFeed,
            binding.vpTabFeed,
            listOf(binding.root),
            childFragmentManager,
            pagerAdapter(listOf(FeedMainFragment(), FeedRecommendationFragment())),
            TAB_TITLES
        )
    }

    private fun showBottomSheetDialog() {
        bottomSheetDialog.setContentView(bottomSheetFilterPostBinding.root)
        bottomSheetDialog.show()

        bottomSheetDialog.setOnDismissListener {
            if (!isCanceled) {
                lifecycleScope.launch { viewModel.getFilteredPost(setSelectedCategory()) }
            }
            isCanceled = true
            binding.btnFilter.clearAnimation()
        }

        bottomSheetFilterPostBinding.btnApply.setOnClickListener {
            isCanceled = false
            bottomSheetDialog.dismiss()
        }
    }

    private fun setSelectedCategory(): Int {
        var selectedCategory = -1
        val checkedButtonName = bottomSheetFilterPostBinding.rgFilterCategory
            .findViewById<RadioButton>(checkedButtonId).text.toString()
        categories.forEach {
            if (it == checkedButtonName) {
                val itIndex = categories.indexOf(it)
                selectedCategory = categoryIds[itIndex]
            }
        }
        return selectedCategory
    }
}