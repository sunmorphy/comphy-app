package com.comphy.photo.ui.event.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.ActivityEventDetailBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.event.detail.fragment.EventDescriptionFragment
import com.comphy.photo.ui.event.detail.fragment.EventImplementationFragment
import com.comphy.photo.utils.Extension.pagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.startActivity
import splitties.resources.drawable

@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.string_event_desc,
            R.string.string_event_impl
        )
        private const val EXTRA_EVENT = "extra_event"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityEventDetailBinding.inflate(layoutInflater)
    }
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private var extraEvent: EventResponseContentItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraEvent = intent.getParcelableExtra(EXTRA_EVENT)

        if (extraEvent == null) finish()
        else setupWidgets()
    }

    private fun setupWidgets() {
        with(binding) {
            Glide.with(this@EventDetailActivity)
                .load(extraEvent!!.linkBanner)
                .placeholder(drawable(R.drawable.img_banner_placeholder))
                .error(drawable(R.drawable.img_banner_placeholder))
                .into(imgEvent)

            viewPagerSetupHelper.setup(
                tabEvent,
                vpTabFeed,
                supportFragmentManager,
                pagerAdapter(listOf(EventDescriptionFragment(), EventImplementationFragment())),
                TAB_TITLES
            )
            btnVisitEvent.setOnClickListener {
                startActivity(Intent.ACTION_VIEW) {
                    data =
                        Uri.parse("https://comphy-staging.herokuapp.com/swagger-ui/index.html")
                }
            }
        }
    }
}