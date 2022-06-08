package com.comphy.photo.ui.main.fragment.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.BottomSheetBinding
import com.comphy.photo.databinding.FragmentTrainingBinding
import com.comphy.photo.ui.event.all.AllEventActivity
import com.comphy.photo.ui.event.detail.EventDetailActivity
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.utils.Extension.changeColor
import com.comphy.photo.vo.EventType.EVENT_ARTICLE
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.toast.toast

class TrainingFragment : Fragment() {

    companion object {
        private const val EXTRA_EVENT = "extra_event"
    }

    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetBinding by lazy(LazyThreadSafetyMode.NONE) { BottomSheetBinding.inflate(layoutInflater) }
    private val viewModel: TrainingViewModel by activityViewModels()
    private var trainingAdapter: TrainingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getEvents() }
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.eventResponse.observe(viewLifecycleOwner) {
            val events = mutableListOf<EventResponseContentItem>()
            it.forEach { event -> if (event.typeEvent != EVENT_ARTICLE) events.add(event) }
            setupViewPager(events)
            setupWidgets(events)
        }
        viewModel.successResponse.observe(viewLifecycleOwner) {
            trainingAdapter!!.notifyItemRemoved(it)
        }
    }

    private fun setupWidgets(events: List<EventResponseContentItem>) {

        trainingAdapter = TrainingAdapter(
            events,
            onBookmarkClick = { eventId, isBookmarked, pos ->
                toast(" THIS FEATURE IS NOT READY YET ")
                /***
                 *
                 * UNCOMMENT COMMAND BELOW FOR UPDATE FEATURE
                 *
                 * lifecycleScope.launch {
                 * if (isBookmarked) viewModel.unbookmarkEvent(eventId, pos)
                 * else viewModel.bookmarkEvent(eventId, pos)
                 * }
                 *
                 */
            },
            onClick = { start<EventDetailActivity> { putExtra(EXTRA_EVENT, it) } }
        )

        with(binding) {
            edtSearch.setOnClickListener { start<AllEventActivity>() }
            btnSeeAllEvent.setOnClickListener { start<AllEventActivity>() }
            btnCourse.setOnClickListener {
                // COURSE ENDPOINT IS NOT READY YET
                // start<CourseActivity>()
                showBottomSheetBlockMiniCourse()
            }
            rvAllEvent.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = trainingAdapter
            }
            rvNewestEvent.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = trainingAdapter
            }
        }
    }

    private fun setupViewPager(listImages: List<EventResponseContentItem>) {
        if (listImages.isNotEmpty()) {
            val pagerAdapter = TrainingPagerAdapter(listImages)
            binding.vpTraining.apply {
                adapter = pagerAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)

                        val job = lifecycleScope.launch {
                            delay(3000)
                            if (binding.vpTraining.currentItem == listImages.size - 1) {
                                binding.vpTraining.setCurrentItem(0, true)

                            } else {
                                binding.vpTraining.setCurrentItem(
                                    binding.vpTraining.currentItem + 1,
                                    true
                                )
                            }
                        }
                        if (job.isActive) job.cancel()
                        else job.start()
                    }
                })
            }
            binding.vpTrainingIndicator.apply {
                setSliderColor(
                    requireActivity().changeColor(R.color.normal_event_pager_indicator),
                    requireActivity().changeColor(R.color.white)
                )
                setSliderWidth(resources.getDimension(R.dimen.dp_9))
                setSliderHeight(resources.getDimension(R.dimen.dp_9))
                setIndicatorGap(resources.getDimension(R.dimen.dp_10))
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.CIRCLE)
                setupWithViewPager(binding.vpTraining)
            }
        }
    }

    private fun showBottomSheetBlockMiniCourse() {
        with(bottomSheetBinding) {
            animView.setAnimation(R.raw.anim_blocked)
            txtSheetTitle.text = getString(R.string.string_feature_blocked)
            txtSheetDesc.text = getString(R.string.string_feature_blocked_desc)
            btnSheetAction.apply {
                text = getString(R.string.string_back)
                setOnClickListener { (activity as MainActivity).bottomSheetDialog.dismiss() }
            }
        }
        (activity as MainActivity).bottomSheetDialog.setContentView(bottomSheetBinding.root)
        (activity as MainActivity).bottomSheetDialog.show()
    }
}