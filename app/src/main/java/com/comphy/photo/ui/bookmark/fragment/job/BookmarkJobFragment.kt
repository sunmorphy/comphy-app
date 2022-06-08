package com.comphy.photo.ui.bookmark.fragment.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentBookmarkJobBinding
import com.comphy.photo.ui.bookmark.BookmarkViewModel
import kotlinx.coroutines.launch
import splitties.toast.toast

class BookmarkJobFragment : Fragment() {

    private var _binding: FragmentBookmarkJobBinding? = null
    val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by activityViewModels()
    private var bookmarkJobAdapter: BookmarkJobAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getBookmarkedJobs() }

        setupWidgets()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.jobResponse.observe(viewLifecycleOwner) { bookmarkJobAdapter!!.setData(it) }
    }

    private fun setupWidgets() {
        bookmarkJobAdapter = BookmarkJobAdapter {
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