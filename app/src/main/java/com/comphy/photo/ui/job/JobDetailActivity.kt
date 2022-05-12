package com.comphy.photo.ui.job

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityJobDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class JobDetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.string_job_description,
            R.string.string_job_requirement
        )
    }

    private lateinit var binding: ActivityJobDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = JobPagerAdapter(this)
        binding.vpTabJob.adapter = pagerAdapter

        TabLayoutMediator(binding.tabJob, binding.vpTabJob) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.vpTabJob.offscreenPageLimit = 2
        binding.vpTabJob.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val myFragment = supportFragmentManager.findFragmentByTag("f$position")
                myFragment?.view?.let { updatePagerHeightForChild(it, binding.vpTabJob) }
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