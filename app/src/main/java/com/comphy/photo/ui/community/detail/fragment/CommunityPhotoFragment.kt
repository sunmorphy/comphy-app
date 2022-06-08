package com.comphy.photo.ui.community.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentCommunityPhotoBinding
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.community.detail.CommunityDetailViewModel
import com.comphy.photo.ui.community.detail.adapter.CommunityDetailPhotoAdapter
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.vo.FollowType
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.toast.toast

class CommunityPhotoFragment : Fragment() {

    companion object {
        private const val EXTRA_ID = "extra_id"
    }

    private var _binding: FragmentCommunityPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityDetailViewModel by activityViewModels()
    private var communityDetailPhotoAdapter: CommunityDetailPhotoAdapter? = null
    private var communityDetailPhotoAdapterItemCount: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentItem = (activity as CommunityDetailActivity).contentItem!!

        lifecycleScope.launch { viewModel.getCommunityPhotos(contentItem.id) }

        communityDetailPhotoAdapter = CommunityDetailPhotoAdapter(
            onProfileClick = {userId -> start<ProfileActivity> { putExtra(EXTRA_ID, userId) } },
            onFollowClick = {userId, isFollowed ->
                lifecycleScope.launch {
                    when (isFollowed) {
                        FollowType.NOT_FOLLOWED -> viewModel.followUser(userId)
                        FollowType.FOLLOWED -> viewModel.unfollowUser(userId)
                    }
                }
            }
        )

        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.communityPhotos.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                communityDetailPhotoAdapter!!.submitData(it)
                communityDetailPhotoAdapter!!.loadStateFlow.collect {
                    communityDetailPhotoAdapterItemCount = communityDetailPhotoAdapter!!.itemCount
                }
            }
        }
        viewModel.followResponse.observe(viewLifecycleOwner) {
            communityDetailPhotoAdapter!!.refresh()
            binding.rvCommunityPhoto.clearFocus()
        }
    }

    private fun setupWidgets() {
//        if (communityDetailPhotoAdapterItemCount < 1) {
//            binding.layoutCommunityPhotoEmpty.visibility = View.VISIBLE
//            binding.rvCommunityPhoto.visibility = View.GONE
//        } else {
//            binding.layoutCommunityPhotoEmpty.visibility = View.GONE
            binding.rvCommunityPhoto.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = communityDetailPhotoAdapter
            }
//        }
    }

    override fun onResume() {
        super.onResume()

        (activity as CommunityDetailActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        communityDetailPhotoAdapter = null
    }
}