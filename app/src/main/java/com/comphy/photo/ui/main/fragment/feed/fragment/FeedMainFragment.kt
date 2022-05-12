package com.comphy.photo.ui.main.fragment.feed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentFeedMainBinding
import com.comphy.photo.ui.main.fragment.feed.FeedAdapter

class FeedMainFragment : Fragment() {

    private var _binding: FragmentFeedMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvVideo.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = FeedAdapter()
        }
    }
}