package com.comphy.photo.ui.community.join.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentJoinCommunityCategoryBinding
import com.comphy.photo.ui.community.join.JoinCommunityAdapter

class JoinCommunityCategoryFragment : Fragment() {

    private var _binding: FragmentJoinCommunityCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJoinCommunityCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCommunitySearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = JoinCommunityAdapter()
        }
    }
}