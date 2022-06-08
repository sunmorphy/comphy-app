package com.comphy.photo.ui.job.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.R
import com.comphy.photo.databinding.FragmentJobBinding
import com.comphy.photo.databinding.FragmentJobDescriptionBinding
import com.comphy.photo.ui.job.JobDescAdapter
import com.comphy.photo.ui.job.JobDetailActivity
import com.comphy.photo.ui.job.JobDetailViewModel

class JobDescriptionFragment : Fragment() {

    private var _binding: FragmentJobDescriptionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JobDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJobDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.jobDetailResponse.observe(viewLifecycleOwner) {
            binding.rvJobDesc.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = JobDescAdapter(it.description)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as JobDetailActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }
}