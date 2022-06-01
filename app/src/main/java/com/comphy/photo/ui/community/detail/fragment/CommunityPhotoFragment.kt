package com.comphy.photo.ui.community.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.databinding.FragmentCommunityPhotoBinding
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.community.detail.CommunityDetailViewModel
import com.comphy.photo.ui.community.detail.adapter.CommunityDetailPhotoAdapter
import kotlinx.coroutines.launch

class CommunityPhotoFragment : Fragment() {

    private var _binding: FragmentCommunityPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityDetailViewModel by activityViewModels()
    private var communityDetailPhotoAdapter: CommunityDetailPhotoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentItem = (activity as CommunityDetailActivity).contentItem!!

        lifecycleScope.launch { viewModel.getCommunityPhotos(contentItem.id) }

        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.communityPhotos.observe(viewLifecycleOwner) {
            lifecycleScope.launch { communityDetailPhotoAdapter?.submitData(it) }
        }
    }

    private fun setupWidgets() {
        if (communityDetailPhotoAdapter?.itemCount == 0) {
            binding.layoutCommunityPhotoEmpty.visibility = View.VISIBLE
            binding.rvCommunityPhoto.visibility = View.GONE
        } else {
            binding.layoutCommunityPhotoEmpty.visibility = View.GONE
            binding.rvCommunityPhoto.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = communityDetailPhotoAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        communityDetailPhotoAdapter = null
    }
}