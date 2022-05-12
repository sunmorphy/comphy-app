package com.comphy.photo.ui.main.fragment.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.comphy.photo.R
import com.comphy.photo.databinding.FragmentFeedBinding
import com.comphy.photo.ui.comment.CommentActivity
import com.google.android.material.tabs.TabLayoutMediator
import splitties.fragments.start

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

        val pagerAdapter = FeedPagerAdapter(this)
        binding.vpTabFeed.adapter = pagerAdapter

        TabLayoutMediator(binding.tabFeed, binding.vpTabFeed) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.inputLayoutWritePost.suffixTextView.setOnClickListener { start<CommentActivity>() }

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