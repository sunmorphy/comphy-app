package com.comphy.photo.ui.main.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.comphy.photo.R
import com.comphy.photo.data.model.CommunityCategoryModel
import com.comphy.photo.data.model.CommunityPreviewModel
import com.comphy.photo.databinding.FragmentHomeBinding
import com.comphy.photo.ui.community.CreateCommunityActivity
import com.comphy.photo.ui.main.fragment.home.adapter.*
import com.comphy.photo.vo.CommunityType
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.fragments.start

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mList = listOf(
            "https://placeimg.com/640/480/people/grayscale",
            "https://placeimg.com/640/480/tech/grayscale",
            "https://placeimg.com/640/480/nature"
        )

        val communities = listOf(
            CommunityPreviewModel(
                "https://placeimg.com/640/480/people/grayscale",
                "First Community",
                "Uhh idk"
            ),
            CommunityPreviewModel(
                "https://placeimg.com/640/480/tech/grayscale",
                "Second Community",
                "yea, this is second"
            ),
            CommunityPreviewModel(
                "https://placeimg.com/640/480/tech/grayscale",
                "Third Community",
                "alright, that's the last one"
            )
        )

        val categories = listOf(
            CommunityCategoryModel(R.drawable.ic_category_1, "Human Interest Photography"),
            CommunityCategoryModel(R.drawable.ic_category_2, "Portrait Photography"),
            CommunityCategoryModel(R.drawable.ic_category_1, "Landscape Photography"),
            CommunityCategoryModel(R.drawable.ic_category_2, "Fashion Photography"),
            CommunityCategoryModel(R.drawable.ic_category_1, "Journalism Photography"),
        )

        setupViewPager(mList)
        setupRecycler(communities, categories)

        binding.btnCreateCommunity.setOnClickListener { start<CreateCommunityActivity>() }
    }

    private fun setupViewPager(listImages: List<String>) {
        val pagerAdapter = HomePagerAdapter(listImages)
        binding.vpHome.apply {
            adapter = pagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    lifecycleScope.launch {
                        delay(5000)
                        if (binding.vpHome.currentItem == listImages.size - 1) {
                            binding.vpHome.setCurrentItem(0, true)

                        } else {
                            binding.vpHome.setCurrentItem(binding.vpHome.currentItem + 1, true)
                        }
                    }
                }
            })
        }
        binding.vpHomeIndicator.apply {
            setSliderColor(
                ContextCompat.getColor(requireActivity(), R.color.normal_pager_indicator),
                ContextCompat.getColor(requireActivity(), R.color.checked_pager_indicator)
            )
            setSliderWidth(resources.getDimension(R.dimen.dp_9))
            setSliderHeight(resources.getDimension(R.dimen.dp_9))
            setIndicatorGap(resources.getDimension(R.dimen.dp_10))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setupWithViewPager(binding.vpHome)
        }
    }

    private fun setupRecycler(
        communities: List<CommunityPreviewModel>,
        categories: List<CommunityCategoryModel>
    ) {
        binding.rvCommunityCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryAdapter(categories)
        }
        binding.rvYourCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommunityPreviewAdapter(communities, CommunityType.OWN)
        }
        binding.rvFollowedCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommunityPreviewAdapter(communities, CommunityType.FOLLOWED)
        }
        binding.rvOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = OfferAdapter()
        }
        binding.rvTips.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = TipsAdapter()
        }
    }
}