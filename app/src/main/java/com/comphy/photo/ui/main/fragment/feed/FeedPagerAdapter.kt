package com.comphy.photo.ui.main.fragment.feed

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comphy.photo.ui.main.fragment.feed.fragment.FeedMainFragment
import com.comphy.photo.ui.main.fragment.feed.fragment.FeedRecommendationFragment

class FeedPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FeedMainFragment()
            1 -> fragment = FeedRecommendationFragment()
        }
        return fragment as Fragment
    }
}