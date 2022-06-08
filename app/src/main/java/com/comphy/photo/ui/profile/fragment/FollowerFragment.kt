package com.comphy.photo.ui.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentFollowerBinding
import com.comphy.photo.ui.profile.FollowersAdapter
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()
    private var followersAdapter: FollowersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.userFollowers.observe(viewLifecycleOwner) {
            followersAdapter = FollowersAdapter(it) { pos, userId, isFollowed ->
                lifecycleScope.launch {
                    if (isFollowed) viewModel.unfollowUser(userId, pos)
                    else viewModel.followUser(userId, pos)
                }
            }
            binding.rvFollowerUsers.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = followersAdapter
            }
        }
        viewModel.followPositionResponse.observe(viewLifecycleOwner) {
            followersAdapter!!.notifyItemChanged(it)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }
}