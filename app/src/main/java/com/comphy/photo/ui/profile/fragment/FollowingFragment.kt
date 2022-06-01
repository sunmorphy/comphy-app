package com.comphy.photo.ui.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentFollowingBinding
import com.comphy.photo.ui.profile.FollowAdapter
import com.comphy.photo.ui.profile.ProfileViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userFollowing.observe(viewLifecycleOwner) {
            binding.rvFollowingUsers.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowAdapter(it)
            }
        }
    }
}