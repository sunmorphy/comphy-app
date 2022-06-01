package com.comphy.photo.ui.main.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.model.CommunityCategoryModel
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.BottomSheetCommunitySetBinding
import com.comphy.photo.databinding.FragmentHomeBinding
import com.comphy.photo.ui.community.all.AllCommunityActivity
import com.comphy.photo.ui.community.create.CreateCommunityActivity
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.ui.main.MainViewModel
import com.comphy.photo.ui.main.fragment.home.adapter.*
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.search.explore.main.ExploreActivity
import com.comphy.photo.utils.Extension.changeColor
import com.comphy.photo.utils.Extension.changeDrawable
import com.comphy.photo.utils.Extension.formatLocationInput
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.toast.toast

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val bottomSheetOptionBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetCommunitySetBinding.inflate(layoutInflater)
    }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme)
    }

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

        lifecycleScope.launch {
            viewModel.getEvents()
            viewModel.getCreatedCommunities()
            viewModel.getJoinedCommunities()
        }

        setupObserver()

        val mList = listOf(
            "https://placeimg.com/640/480/people/grayscale",
            "https://placeimg.com/640/480/tech/grayscale",
            "https://placeimg.com/640/480/nature"
        )

        val categories = listOf(
            CommunityCategoryModel(R.drawable.ic_category_1, "Human Interest Photography"),
            CommunityCategoryModel(R.drawable.ic_category_2, "Portrait Photography"),
            CommunityCategoryModel(R.drawable.ic_category_3, "Landscape Photography"),
            CommunityCategoryModel(R.drawable.ic_category_4, "Fashion Photography"),
            CommunityCategoryModel(R.drawable.ic_category_5, "Journalism Photography"),
        )

        setupRecycler(categories)

        binding.imgProfile.setOnClickListener {
            start<ProfileActivity> {
                putExtra("extra_id", (activity as MainActivity).userAuth.userId)
            }
        }
        binding.edtSearch.setOnClickListener { start<ExploreActivity>() }
        binding.btnCreateCommunity.setOnClickListener { start<CreateCommunityActivity>() }
        binding.btnSeeAllYours.setOnClickListener {
            start<AllCommunityActivity> {
                putExtra(
                    "extra_own",
                    "extra_own"
                )
            }
        }
        binding.btnSeeAllFollowed.setOnClickListener {
            start<AllCommunityActivity> {
                putExtra(
                    "extra_followed",
                    "extra_followed"
                )
            }
        }

        binding.edtSearch.setOnClickListener { start<ExploreActivity>() }
    }

    private fun setupObserver() {
        mainViewModel.userData.observe(viewLifecycleOwner) {
            binding.txtLocation.text = formatLocationInput(it.location!!)
            Glide.with(this)
                .load(it.profileUrl)
                .placeholder(activity?.changeDrawable(R.drawable.ic_placeholder_people))
                .error(activity?.changeDrawable(R.drawable.ic_placeholder_people))
                .centerCrop()
                .into(binding.imgProfile)
        }
        viewModel.events.observe(viewLifecycleOwner) { setupViewPager(it) }
        viewModel.userCreatedCommunity.observe(viewLifecycleOwner) { setCommunitiesEmpty(it, true) }
        viewModel.userJoinedCommunity.observe(viewLifecycleOwner) { setCommunitiesEmpty(it, false) }
        viewModel.leaveResponse.observe(viewLifecycleOwner) { bottomSheetDialog.dismiss() }
        viewModel.exceptionResponse.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun setupViewPager(listImages: List<EventResponseContentItem>) {
        val pagerAdapter = HomePagerAdapter(listImages)
        binding.vpHome.apply {
            adapter = pagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val job = lifecycleScope.launch {
                        delay(3000)
                        if (binding.vpHome.currentItem == listImages.size - 1) {
                            binding.vpHome.setCurrentItem(0, true)

                        } else {
                            binding.vpHome.setCurrentItem(binding.vpHome.currentItem + 1, true)
                        }
                    }
                    if (job.isActive) job.cancel()
                    else job.start()
                }
            })
        }
        binding.vpHomeIndicator.apply {
            setSliderColor(
                requireActivity().changeColor(R.color.normal_pager_indicator),
                requireActivity().changeColor(R.color.checked_pager_indicator)
            )
            setSliderWidth(resources.getDimension(R.dimen.dp_9))
            setSliderHeight(resources.getDimension(R.dimen.dp_9))
            setIndicatorGap(resources.getDimension(R.dimen.dp_10))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setupWithViewPager(binding.vpHome)
        }
    }

    private fun setupRecycler(categories: List<CommunityCategoryModel>) {
        binding.rvCommunityCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryAdapter(categories)
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

    private fun setCommunitiesEmpty(
        communities: List<FollowCommunityResponseContentItem>,
        isCreated: Boolean
    ) {
        if (communities.isEmpty()) {
            if (isCreated) {
                binding.layoutYourCommunityEmpty.visibility = View.VISIBLE
                binding.layoutYourCommunity.visibility = View.INVISIBLE
                binding.rvYourCommunity.apply {
                    visibility = View.INVISIBLE
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = CommunityPreviewAdapter(null, {}, {})
                }
            } else {
                binding.layoutFollowedCommunityEmpty.visibility = View.VISIBLE
                binding.layoutFollowedCommunity.visibility = View.INVISIBLE
                binding.rvFollowedCommunity.apply {
                    visibility = View.INVISIBLE
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = CommunityPreviewAdapter(null, {}, {})
                }
            }
        } else {
            if (isCreated) {
                if (communities.size <= 1) {
                    binding.layoutCommunityLimit.visibility = View.GONE
                    binding.layoutCommunityAdd.apply {
                        visibility = View.VISIBLE
                        setOnClickListener { start<CreateCommunityActivity>() }
                    }
                } else {
                    binding.layoutCommunityAdd.visibility = View.GONE
                    binding.layoutCommunityLimit.visibility = View.VISIBLE
                }
                binding.layoutYourCommunityEmpty.visibility = View.GONE
                binding.layoutYourCommunity.visibility = View.VISIBLE
                binding.rvYourCommunity.apply {
                    visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = CommunityPreviewAdapter(
                        communities,
                        { communityId ->
                            bottomSheetOptionBinding.apply {
                                txtCommunitySettings.apply {
                                    visibility = View.VISIBLE
                                    setOnClickListener { toast("COMMUNITY SETTING NOT SETTED YET") }
                                }
                                divider.visibility = View.VISIBLE
                                txtCommunityLeave.setOnClickListener {
                                    lifecycleScope.launch { viewModel.leaveCommunity(communityId) }
                                }
                            }
                            bottomSheetDialog.setContentView(bottomSheetOptionBinding.root)
                            bottomSheetDialog.show()
                        },
                        {
                            start<CommunityDetailActivity> {
                                putExtra("extra_detail", it)
                            }
                        }
                    )
                }
            } else {
                binding.layoutFollowedCommunityEmpty.visibility = View.GONE
                binding.layoutFollowedCommunity.visibility = View.VISIBLE
                binding.rvFollowedCommunity.apply {
                    visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = CommunityPreviewAdapter(
                        communities,
                        { communityId ->
                            bottomSheetOptionBinding.apply {
                                txtCommunitySettings.visibility = View.GONE
                                divider.visibility = View.GONE
                                txtCommunityLeave.setOnClickListener {
                                    lifecycleScope.launch { viewModel.leaveCommunity(communityId) }
                                }
                            }
                            bottomSheetDialog.setContentView(bottomSheetOptionBinding.root)
                            bottomSheetDialog.show()
                        },
                        {
                            start<CommunityDetailActivity> {
                                putExtra("extra_id", it)
                            }
                        }
                    )
                }
            }
        }
    }
}