package com.comphy.photo.ui.onboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.comphy.photo.R
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.databinding.ActivityOnboardBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.auth.register.RegisterActivity
import com.comphy.photo.ui.biodata.BiodataActivity
import com.comphy.photo.ui.main.MainActivity
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start
import javax.inject.Inject

@AndroidEntryPoint
class OnboardActivity : AppCompatActivity() {
    @Inject
    lateinit var userAuth: UserAuth

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityOnboardBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<OnboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userAuth.isLogin) {
            if (userAuth.isUserUpdated) {
                start<MainActivity>()
            } else {
                start<BiodataActivity>()
            }
            finish()
        }

        setContentView(binding.root)

        viewModel.listAssets.observe(this) {
            setupViewPager(it.image, it.title, it.desc)
        }
    }

    private fun setupViewPager(
        listImages: List<Int>,
        listTitles: List<Int>,
        listDescriptions: List<Int>
    ) {
        val pagerAdapter = OnboardPagerAdapter(listImages)
        binding.vpOnboard.apply {
            adapter = pagerAdapter
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                    repeat(3) {
                        if (position == it) {
                            binding.txtOnboardTitle.text = resources.getString(listTitles[position])
                            binding.txtOnboardDescription.text =
                                resources.getString(listDescriptions[position])
                            setButtonOnboard(position)
                        }
                    }
                }
            })
        }

        binding.vpOnboardIndicator.apply {
            setSliderColor(
                ContextCompat.getColor(this@OnboardActivity, R.color.normal_pager_indicator),
                ContextCompat.getColor(this@OnboardActivity, R.color.checked_pager_indicator)
            )
            setSliderWidth(resources.getDimension(R.dimen.dp_9))
            setSliderHeight(resources.getDimension(R.dimen.dp_9))
            setIndicatorGap(resources.getDimension(R.dimen.dp_10))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setupWithViewPager(binding.vpOnboard)
        }
    }

    private fun setButtonOnboard(position: Int) {
        when {
            position < 2 -> {
                binding.btnOnboardNext.apply {
                    text = resources.getText(R.string.button_onboard_next)
                    setOnClickListener {
                        binding.vpOnboard.setCurrentItem(position + 1, true)
                    }
                }
                binding.btnOnboardSkip.apply {
                    text = resources.getText(R.string.button_onboard_skip)
                    setOnClickListener { binding.vpOnboard.setCurrentItem(2, true) }
                }
            }
            position == 2 -> {
                binding.btnOnboardNext.apply {
                    text = resources.getText(R.string.string_login)
                    setOnClickListener { start<LoginActivity>() }
                }
                binding.btnOnboardSkip.apply {
                    text = resources.getText(R.string.string_register)
                    setOnClickListener { start<RegisterActivity>() }
                }
            }
        }
    }
}