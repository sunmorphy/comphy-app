package com.comphy.photo.ui.main.fragment.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.FragmentFeedBinding
import com.comphy.photo.ui.post.CreatePostActivity
import com.comphy.photo.utils.Extension
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import splitties.fragments.start
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
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by activityViewModels()

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

        lifecycleScope.launch { viewModel.getUserDetails() }

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

        viewModel.userData.observe(viewLifecycleOwner) {
            binding.txtLocation.text = Extension.formatLocationInput(it.location!!)
            Glide.with(this)
                .load(it.profileUrl)
                .placeholder(activity?.drawable(R.drawable.ic_placeholder_people))
                .error(activity?.drawable(R.drawable.ic_placeholder_people))
                .centerCrop()
                .into(binding.imgProfile)
        }

        val pagerAdapter = FeedPagerAdapter(this)
        binding.vpTabFeed.adapter = pagerAdapter

        TabLayoutMediator(binding.tabFeed, binding.vpTabFeed) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        setupViewPager()
    }

    private fun setupViewPager() {
        binding.vpTabFeed.offscreenPageLimit = 2
        binding.vpTabFeed.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val myFragment = childFragmentManager.findFragmentByTag("f$position")
                myFragment?.view?.let { updatePagerHeightForChild(it, binding.vpTabFeed) }
            }

            private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
                view.post {
                    val wMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)

                    if (pager.layoutParams.height != view.measuredHeight) {
                        pager.layoutParams = (pager.layoutParams)
                            .also { lp ->
                                lp.height = view.measuredHeight
                            }
                    }
                }
            }
        })
    }
}