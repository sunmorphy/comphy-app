package com.comphy.photo.ui.bookmark.fragment.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentBookmarkEventBinding
import com.comphy.photo.ui.bookmark.BookmarkViewModel
import com.comphy.photo.ui.event.all.AllEventAdapter
import com.comphy.photo.ui.event.detail.EventDetailActivity
import kotlinx.coroutines.launch
import splitties.fragments.start

class BookmarkEventFragment : Fragment() {

    companion object {
        private const val EXTRA_EVENT = "extra_event"
    }

    private var _binding: FragmentBookmarkEventBinding? = null
    val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by activityViewModels()
    private var bookmarkEventAdapter: AllEventAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getBookmarkedEvents() }

        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.eventResponse.observe(viewLifecycleOwner) {
            bookmarkEventAdapter!!.setData(it)
            if (bookmarkEventAdapter!!.events.isEmpty()) {
                binding.rvEvent.visibility = View.GONE
                binding.layoutBookmarkEmpty.visibility = View.VISIBLE
            } else {
                binding.rvEvent.visibility = View.VISIBLE
                binding.layoutBookmarkEmpty.visibility = View.GONE
            }
        }
    }

    private fun setupWidgets() {
        bookmarkEventAdapter = AllEventAdapter {
            start<EventDetailActivity> { putExtra(EXTRA_EVENT, it) }
        }
        with(binding) {
            rvEvent.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = bookmarkEventAdapter
            }
        }
    }
}