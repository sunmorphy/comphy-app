package com.comphy.photo.ui.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.comphy.photo.databinding.FragmentProfileAboutBinding
import com.comphy.photo.ui.profile.ProfileActivity
import com.comphy.photo.ui.profile.ProfileViewModel

class ProfileAboutFragment : Fragment() {

    private var _binding: FragmentProfileAboutBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWidgets()
        setupObserver()
    }

    private fun setupWidgets() {
        val extraId = (activity as ProfileActivity).extraId
        val userId = (activity as ProfileActivity).userAuth.userId
        if (extraId == userId) {
            binding.btnAddExperience.visibility = View.VISIBLE
        } else {
            binding.btnAddExperience.visibility = View.INVISIBLE
        }
    }

    private fun setupObserver() {
        profileViewModel.userData.observe(viewLifecycleOwner) {
            binding.txtAboutContent.text = it.description
        }
    }
}