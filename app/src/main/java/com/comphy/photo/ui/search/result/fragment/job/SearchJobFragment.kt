package com.comphy.photo.ui.search.result.fragment.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentSearchJobBinding
import com.comphy.photo.ui.search.result.SearchResultViewModel
import kotlinx.coroutines.launch
import splitties.toast.toast

class SearchJobFragment : Fragment() {

    private var _binding: FragmentSearchJobBinding? = null
    val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by activityViewModels()
    private var searchJobAdapter: SearchJobAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            // TODO: LAUNCH GET JOBS
        }

        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.jobsResponse.observe(viewLifecycleOwner) { searchJobAdapter!!.setData(it) }
    }

    private fun setupWidgets() {
        searchJobAdapter = SearchJobAdapter {
            toast(" THIS FEATURE IS NOT READY YET ")
            // TODO: INTENT TO DETAIL JOB
        }
        with(binding) {
            rvJob.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter
            }
        }
    }
}