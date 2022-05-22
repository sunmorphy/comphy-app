package com.comphy.photo.ui.main.fragment.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.BottomSheetFilterJobBinding
import com.comphy.photo.databinding.FragmentJobBinding
import com.comphy.photo.ui.job.JobDetailActivity
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.changeDrawable
import com.comphy.photo.utils.Extension.formatCity
import com.comphy.photo.utils.Extension.loadAnim
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import splitties.fragments.start

class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetFilterJobBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFilterJobBinding.inflate(layoutInflater)
    }

    private val viewModel: JobViewModel by activityViewModels()
    private var jobLocation = ""
    private var jobType = ""
    private var isCanceled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.getJobs()
            viewModel.getCities()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch { viewModel.getJobs() }

        binding.btnFilter.setOnClickListener {
            binding.btnFilter.startAnimation(
                activity?.loadAnim(R.anim.btn_filter_anim)
            )
            showBottomSheetDialog()
        }

        setupObserver()
    }

    private fun setupObserver() {
//        viewModel.isLoading.observe(viewLifecycleOwner) {
//            (activity as MainActivity).setLoading(it)
//        }
        viewModel.cities.observe(viewLifecycleOwner) {
            val locationAdapter =
                ArrayAdapter(
                    requireContext(),
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    formatCity(it)
                )

            bottomSheetFilterJobBinding.edtLocation.apply {
                setDropDownBackgroundDrawable(activity?.changeDrawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
        viewModel.jobResponse.observe(viewLifecycleOwner) {
            setupRecycler(it.jobResponseData?.content!!)
        }
    }

    private fun setupRecycler(listJobs: List<Any>) {
        if (listJobs.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvJob.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvJob.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = JobAdapter(listJobs) { start<JobDetailActivity>() }
            }
        }
    }

    private fun showBottomSheetDialog() {
        bottomSheetDialog.setContentView(bottomSheetFilterJobBinding.root)
        bottomSheetDialog.show()

        bottomSheetDialog.setOnDismissListener {
            if (!isCanceled) {
                if (bottomSheetFilterJobBinding.edtLocation.text != null
                    && bottomSheetFilterJobBinding.rgFilterJobType.checkedRadioButtonId != -1
                ) {
                    jobLocation =
                        bottomSheetFilterJobBinding.edtLocation.text.toString().split(",")[0]
                    jobType =
                        bottomSheetFilterJobBinding.rgFilterJobType.findViewById<RadioButton>(
                            bottomSheetFilterJobBinding.rgFilterJobType.checkedRadioButtonId
                        ).text.toString()

                    lifecycleScope.launch {
                        when {
                            jobType.lowercase().contains("full") -> {
                                viewModel.getFilteredJobs(
                                    region = jobLocation,
                                    isFullTime = true
                                )
                            }
                            jobType.lowercase().contains("part") -> {
                                viewModel.getFilteredJobs(
                                    region = jobLocation,
                                    isPartTime = true
                                )
                            }
                        }
                    }
                }
            }
            isCanceled = true
            binding.btnFilter.clearAnimation()
        }

        bottomSheetFilterJobBinding.edtLocation.doAfterTextChanged { setBottomSheetActionEnable() }
        bottomSheetFilterJobBinding.rgFilterJobType.setOnCheckedChangeListener { _, _ -> setBottomSheetActionEnable() }
    }

    private fun setBottomSheetActionEnable() {
        bottomSheetFilterJobBinding.btnApply.isEnabled =
            (bottomSheetFilterJobBinding.edtLocation.text.isNotEmpty()
                    && bottomSheetFilterJobBinding.rgFilterJobType.checkedRadioButtonId != -1)

        if (bottomSheetFilterJobBinding.btnApply.isEnabled) {
            bottomSheetFilterJobBinding.btnApply.setOnClickListener {
                isCanceled = false
                bottomSheetDialog.dismiss()
            }
        }
    }
}