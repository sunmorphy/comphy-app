package com.comphy.photo.helpers

import android.content.Context
import android.view.View
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

        viewPager2.offscreenPageLimit = 2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val myFragment = fragmentManager.findFragmentByTag("f$position")
                myFragment?.view?.let { updatePagerHeightForChild(it, viewPager2) }
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