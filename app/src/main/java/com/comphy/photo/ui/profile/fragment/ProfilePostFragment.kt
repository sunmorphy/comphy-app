package com.comphy.photo.ui.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.comphy.photo.databinding.FragmentProfilePostBinding
import com.comphy.photo.ui.profile.ProfileActivity

class ProfilePostFragment : Fragment() {

    private var _binding: FragmentProfilePostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfilePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWidgets()
    }

    private fun setupWidgets() {
        val extraId = (activity as ProfileActivity).extraId
        val userId = (activity as ProfileActivity).userAuth.userId
        if (extraId == userId) {
            binding.layoutPost.visibility = View.VISIBLE
        } else {
            binding.layoutPost.visibility = View.GONE
            binding.layoutEmpty.visibility = View.GONE
        }
    }
}