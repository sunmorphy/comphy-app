package com.comphy.photo.ui.search.result.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentSearchCommunityBinding
import com.comphy.photo.ui.search.result.SearchResultActivity
import com.comphy.photo.ui.search.result.SearchResultViewModel
import kotlinx.coroutines.launch
import splitties.toast.toast

class SearchCommunityFragment : Fragment() {

    private var _binding: FragmentSearchCommunityBinding? = null
    val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by activityViewModels()
    private var searchCommunityAdapter: SearchCommunityAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getFilteredCommunities((activity as SearchResultActivity).extraKey!!)
        }
        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.communitiesResponse.observe(viewLifecycleOwner) {
            searchCommunityAdapter!!.setData(it)
        }
    }

    private fun setupWidgets() {
        searchCommunityAdapter = SearchCommunityAdapter {
            toast(" THIS FEATURE IS NOT READY YET ")
            // TODO: INTENT TO COMMUNITY DETAILS
        }
        with(binding) {
            rvCommunity.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchCommunityAdapter
            }
        }
    }
}