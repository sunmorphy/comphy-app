package com.comphy.photo.ui.main.fragment.home

import android.content.Intent
import android.net.Uri
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
import com.comphy.photo.data.model.OfferModel
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.join.JoinedCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.BottomSheetCommunitySetBinding
import com.comphy.photo.databinding.FragmentHomeBinding
import com.comphy.photo.ui.article.ArticleDetailActivity
import com.comphy.photo.ui.community.all.AllCommunityActivity
import com.comphy.photo.ui.community.create.CreateCommunityActivity
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.community.edit.EditCommunityActivity
import com.comphy.photo.ui.community.join.JoinCommunityActivity
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.ui.main.MainViewModel
import com.comphy.photo.ui.main.fragment.home.adapter.*
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.search.explore.main.ExploreActivity
import com.comphy.photo.utils.Extension.changeColor
import com.comphy.photo.utils.Extension.changeDrawable
import com.comphy.photo.utils.Extension.formatLocationInput
import com.comphy.photo.vo.EventType.EVENT_ARTICLE
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.fragments.startActivity
import splitties.toast.toast

class HomeFragment : Fragment() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_OWN = "extra_own"
        private const val EXTRA_FOLLOWED = "extra_followed"
        private const val EXTRA_ARTICLE = "extra_article"
        private const val EXTRA_NEXT_ARTICLE = "extra_next_article"
        private const val EXTRA_DETAIL = "extra_detail"
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
    }

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

        val categories = listOf(
            CommunityCategoryModel(R.drawable.ic_category_1, "Human Interest Photography"),
            CommunityCategoryModel(R.drawable.ic_category_2, "Portrait Photography"),
            CommunityCategoryModel(R.drawable.ic_category_3, "Landscape Photography"),
            CommunityCategoryModel(R.drawable.ic_category_4, "Fashion Photography"),
            CommunityCategoryModel(R.drawable.ic_category_5, "Journalism Photography"),
        )

        val offers = listOf(
            OfferModel(R.drawable.img_shutterstock, "https://www.shutterstock.com/id/"),
            OfferModel(R.drawable.img_istock, "https://www.istockphoto.com/id"),
            OfferModel(R.drawable.img_etsy, "https://www.etsy.com/"),
            OfferModel(R.drawable.img_dreamstime, "https://www.dreamstime.com/"),
            OfferModel(R.drawable.img_foap, "https://www.foap.com/"),
            OfferModel(R.drawable.img_redbubble, "https://www.redbubble.com/"),
            OfferModel(R.drawable.img_500px, "https://500px.com/"),
            OfferModel(R.drawable.img_demand_media, "https://demandmedia.com/")
        )

        setupRecycler(categories, offers)

        binding.imgProfile.setOnClickListener {
            start<ProfileActivity> {
                putExtra(EXTRA_ID, (activity as MainActivity).userAuth.userId)
            }
        }
        binding.edtSearch.setOnClickListener { start<ExploreActivity>() }
        binding.btnCreateCommunity.setOnClickListener { start<CreateCommunityActivity>() }
        binding.btnSeeAllYours.setOnClickListener {
            start<AllCommunityActivity> {
                putExtra(EXTRA_OWN, EXTRA_OWN)
            }
        }
        binding.btnSeeAllFollowed.setOnClickListener {
            start<AllCommunityActivity> {
                putExtra(EXTRA_FOLLOWED, EXTRA_FOLLOWED)
            }
        }

        binding.edtSearch.setOnClickListener { start<ExploreActivity>() }
    }

    private fun setupObserver() {
        mainViewModel.userData.observe(viewLifecycleOwner) {
            binding.txtLocation.text = formatLocationInput(it.location!!)
            Glide.with(this)
                .load(it.profilePhotoLink)
                .placeholder(activity?.changeDrawable(R.drawable.ic_placeholder_people))
                .error(activity?.changeDrawable(R.drawable.ic_placeholder_people))
                .centerCrop()
                .into(binding.imgProfile)
        }
        viewModel.events.observe(viewLifecycleOwner) {
            val articles = mutableListOf<EventResponseContentItem>()
            it.forEach { event -> if (event.typeEvent == EVENT_ARTICLE) articles.add(event) }
            setupViewPager(it)
            binding.rvTips.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = TipsAdapter(articles) { article, nextArticle ->
                    start<ArticleDetailActivity> {
                        putExtra(EXTRA_ARTICLE, article)
                        putExtra(EXTRA_NEXT_ARTICLE, nextArticle)
                    }
                }
            }
        }
        viewModel.userCreatedCommunity.observe(viewLifecycleOwner) { setCreatedCommunitiesEmpty(it) }
        viewModel.userJoinedCommunity.observe(viewLifecycleOwner) { setJoinedCommunitiesEmpty(it) }
        viewModel.leaveResponse.observe(viewLifecycleOwner) {
            bottomSheetDialog.dismiss()
            lifecycleScope.launch {
                viewModel.getCreatedCommunities()
                viewModel.getJoinedCommunities()
            }
        }
        viewModel.exceptionResponse.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun setupViewPager(listImages: List<EventResponseContentItem>) {
        if (listImages.isNotEmpty()) {
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
    }

    private fun setupRecycler(
        categories: List<CommunityCategoryModel>,
        offers: List<OfferModel>
    ) {
        binding.rvCommunityCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryAdapter(categories)
        }
        binding.rvOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = OfferAdapter(offers) {
                startActivity(Intent.ACTION_VIEW) {
                    data = Uri.parse(it)
                }
            }
        }
    }

    private fun setCreatedCommunitiesEmpty(communities: List<FollowCommunityResponseContentItem>) {
        if (communities.isEmpty()) {
            binding.layoutYourCommunityEmpty.apply {
                visibility = View.VISIBLE
                setOnClickListener { start<CreateCommunityActivity>() }
            }
            binding.layoutYourCommunity.visibility = View.INVISIBLE
            binding.rvYourCommunity.apply {
                visibility = View.INVISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowCommunityPreviewAdapter(null, null, {}, {})
            }
        } else {
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
                adapter = FollowCommunityPreviewAdapter(
                    createdCommunities = communities,
                    joinedCommunities = null,
                    { community ->
                        bottomSheetOptionBinding.apply {
                            txtCommunitySettings.apply {
                                visibility = View.VISIBLE
                                setOnClickListener {
                                    bottomSheetDialog.dismiss()
                                    start<EditCommunityActivity> {
                                        putExtra(EXTRA_CONTENT_ITEM, community)
                                    }
                                }
                            }
                            divider.visibility = View.VISIBLE
                            txtCommunityLeave.setOnClickListener {
                                lifecycleScope.launch { viewModel.deleteCommunity(community.id) }
                            }
                        }
                        bottomSheetDialog.setContentView(bottomSheetOptionBinding.root)
                        bottomSheetDialog.show()
                    },
                    {
                        start<CommunityDetailActivity> {
                            putExtra(EXTRA_DETAIL, it)
                        }
                    }
                )
            }
        }
    }

    private fun setJoinedCommunitiesEmpty(communities: List<JoinedCommunityResponseContentItem>) {
        if (communities.isEmpty()) {
            binding.layoutFollowedCommunityEmpty.apply {
                visibility = View.VISIBLE
                setOnClickListener { start<JoinCommunityActivity>() }
            }
            binding.layoutFollowedCommunity.visibility = View.INVISIBLE
            binding.rvFollowedCommunity.apply {
                visibility = View.INVISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowCommunityPreviewAdapter(null, null, {}, {})
            }
        } else {
            binding.layoutFollowedCommunityEmpty.visibility = View.GONE
            binding.layoutFollowedCommunity.visibility = View.VISIBLE
            binding.layoutExploreCommunity.setOnClickListener { start<JoinCommunityActivity>() }
            binding.rvFollowedCommunity.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowCommunityPreviewAdapter(
                    createdCommunities = null,
                    joinedCommunities = communities,
                    { community ->
                        bottomSheetOptionBinding.apply {
                            txtCommunitySettings.visibility = View.GONE
                            divider.visibility = View.GONE
                            txtCommunityLeave.setOnClickListener {
                                lifecycleScope.launch { viewModel.leaveCommunity(community.id) }
                            }
                        }
                        bottomSheetDialog.setContentView(bottomSheetOptionBinding.root)
                        bottomSheetDialog.show()
                    },
                    {
                        start<CommunityDetailActivity> {
                            putExtra(EXTRA_DETAIL, it)
                        }
                    }
                )
            }
        }
    }
}