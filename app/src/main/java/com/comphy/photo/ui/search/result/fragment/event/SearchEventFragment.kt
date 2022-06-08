package com.comphy.photo.ui.search.result.fragment.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentSearchEventBinding
import com.comphy.photo.ui.event.all.AllEventAdapter
import com.comphy.photo.ui.event.detail.EventDetailActivity
import com.comphy.photo.ui.search.result.SearchResultActivity
import com.comphy.photo.ui.search.result.SearchResultViewModel
import kotlinx.coroutines.launch
import splitties.fragments.start

class SearchEventFragment : Fragment() {

    companion object {
        private const val EXTRA_EVENT = "extra_event"
    }

    private var _binding: FragmentSearchEventBinding? = null
    val binding get() = _binding!!

    private val viewModel: SearchResultViewModel by activityViewModels()
    private var searchEventAdapter: AllEventAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getFilteredEvent((activity as SearchResultActivity).extraKey!!)
        }

        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.eventResponse.observe(viewLifecycleOwner) { searchEventAdapter!!.setData(it) }
    }

    private fun setupWidgets() {
        searchEventAdapter = AllEventAdapter {
            start<EventDetailActivity> { putExtra(EXTRA_EVENT, it) }
        }
        with(binding) {
            rvEvent.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchEventAdapter
            }
        }
    }
}