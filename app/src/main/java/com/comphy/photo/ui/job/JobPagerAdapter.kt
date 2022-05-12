package com.comphy.photo.ui.job

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comphy.photo.ui.job.fragment.JobDescriptionFragment
import com.comphy.photo.ui.job.fragment.JobRequirementFragment

class JobPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = JobDescriptionFragment()
            1 -> fragment = JobRequirementFragment()
        }
        return fragment as Fragment
    }
}