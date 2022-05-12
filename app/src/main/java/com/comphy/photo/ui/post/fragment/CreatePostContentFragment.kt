package com.comphy.photo.ui.post.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.comphy.photo.databinding.FragmentCreatePostContentBinding

class CreatePostContentFragment : Fragment() {
    private var _binding: FragmentCreatePostContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePostContentBinding.inflate(inflater, container, false)
        return binding.root
    }
}