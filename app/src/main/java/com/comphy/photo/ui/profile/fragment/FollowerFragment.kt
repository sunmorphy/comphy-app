package com.comphy.photo.ui.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentFollowerBinding
import com.comphy.photo.ui.profile.FollowAdapter
import com.comphy.photo.ui.profile.ProfileViewModel

class FollowerFragment : Fragment() {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

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

        profileViewModel.userFollowers.observe(viewLifecycleOwner) {
            binding.rvFollowerUsers.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowAdapter(it)
            }
        }
    }
}