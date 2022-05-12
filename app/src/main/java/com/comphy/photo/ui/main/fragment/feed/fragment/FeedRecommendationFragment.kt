package com.comphy.photo.ui.main.fragment.feed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.comphy.photo.databinding.FragmentFeedRecommendationBinding

class FeedRecommendationFragment : Fragment() {

    private var _binding: FragmentFeedRecommendationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }
}