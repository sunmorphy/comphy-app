package com.comphy.photo.ui.profile

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comphy.photo.ui.profile.fragment.ProfileAboutFragment
import com.comphy.photo.ui.profile.fragment.ProfilePostFragment

class ProfilePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ProfilePostFragment()
            1 -> fragment = ProfileAboutFragment()
        }
        return fragment as Fragment
    }
}