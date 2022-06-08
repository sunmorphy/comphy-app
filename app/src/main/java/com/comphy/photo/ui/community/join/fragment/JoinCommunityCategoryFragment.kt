package com.comphy.photo.ui.community.join.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentJoinCommunityCategoryBinding
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.community.join.JoinCommunityActivity
import com.comphy.photo.ui.community.join.JoinCommunityAdapter
import com.comphy.photo.ui.community.join.JoinCommunityViewModel
import splitties.fragments.start

class JoinCommunityCategoryFragment : Fragment() {

    companion object {
        private const val EXTRA_DETAIL = "extra_detail"
    }

    private var _binding: FragmentJoinCommunityCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JoinCommunityViewModel by activityViewModels()
    private var joinCommunityAdapter: JoinCommunityAdapter? = null

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

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.communityResponse.observe(viewLifecycleOwner) {
            joinCommunityAdapter = JoinCommunityAdapter { contentItem ->
                start<CommunityDetailActivity> { putExtra(EXTRA_DETAIL, contentItem) }
            }
            joinCommunityAdapter!!.setData(it)
            binding.rvCommunitySearch.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = joinCommunityAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as JoinCommunityActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }
}