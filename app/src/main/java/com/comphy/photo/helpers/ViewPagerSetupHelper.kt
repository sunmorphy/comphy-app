package com.comphy.photo.helpers

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerSetupHelper(
    private val context: Context
) {

    fun setup(
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        fragmentManager: FragmentManager,
        pagerAdapter: FragmentStateAdapter,
        tabTitles: IntArray
    ) {
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = context.resources.getString(tabTitles[position])
        }.attach()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

//                if (position == fragmentManager.fragments.size - 1) {
//                    val myFragment = fragmentManager.fragments[position]
//                    myFragment?.view?.let { pagerAdapter.notifyDataSetChanged() }
//                } else {
//                    pagerAdapter.notifyDataSetChanged()
//                }
                if (fragmentManager.fragments.size > position) {
                    val myFragment = fragmentManager.fragments[position]
                    myFragment?.view?.let { updatePagerHeightForChild(it, viewPager2) }
                }
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
                            .also { lp -> lp.height = view.measuredHeight }
                    }
                }
            }
        })
    }

    fun setupFeed(
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        otherViews: List<View>,
        fragmentManager: FragmentManager,
        pagerAdapter: FragmentStateAdapter,
        tabTitles: IntArray
    ) {
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = context.resources.getString(tabTitles[position])
        }.attach()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private fun listener(view: View?) = ViewTreeObserver.OnGlobalLayoutListener {
                view?.let { updatePagerHeightForChild(it) }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val myFragment = fragmentManager.fragments[position]
                otherViews.forEach {
                    it.viewTreeObserver.removeOnGlobalLayoutListener(listener(myFragment.view))
                }
                myFragment.view?.viewTreeObserver?.addOnGlobalLayoutListener(listener(myFragment.view))
            }

            private fun updatePagerHeightForChild(view: View) {
                view.post {
                    val wMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)

                    if (viewPager2.layoutParams.height != view.measuredHeight) {
                        viewPager2.layoutParams =
                            (viewPager2.layoutParams as LinearLayout.LayoutParams)
                                .also { lp -> lp.height = view.measuredHeight }
                    }
                }
            }
        })
    }

    fun setupNormal(
        tabLayout: TabLayout,
        viewPager2: ViewPager2,
        pagerAdapter: FragmentStateAdapter,
        tabTitles: IntArray
    ) {
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = context.resources.getString(tabTitles[position])
        }.attach()
    }

    fun setProperHeightOfView(view: View) {
        val layoutParams = view.layoutParams
        if (layoutParams != null) {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            view.requestLayout()
        }
    }
}