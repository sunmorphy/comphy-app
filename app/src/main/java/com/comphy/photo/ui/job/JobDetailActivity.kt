package com.comphy.photo.ui.job

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityJobDetailBinding
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.job.fragment.JobDescriptionFragment
import com.comphy.photo.ui.job.fragment.JobRequirementFragment
import com.comphy.photo.utils.Extension.pagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.resources.str

@AndroidEntryPoint
class JobDetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.string_job_description,
            R.string.string_job_requirement
        )
        private const val EXTRA_ID = "extra_id"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityJobDetailBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private val viewModel: JobDetailViewModel by viewModels()
    private var extraId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraId = intent.getIntExtra(EXTRA_ID, -1)

        if (extraId == -1) {
            finish()
        } else {

            lifecycleScope.launch { viewModel.getJobDetails(extraId) }

            setupObserver()
            setupWidgets()
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.jobDetailResponse.observe(this) { jobData ->
            Glide.with(this)
                .load("")
                .placeholder(R.drawable.ic_placeholder_people)
                .error(R.drawable.ic_placeholder_people)
                .centerCrop()
                .into(binding.imgJobLogo)

            binding.txtJobTitle.text = jobData.title
            binding.txtJobCompany.text = jobData.companyName
            binding.txtJobSalary.text = jobData.rangeSalary
            binding.txtJobLocation.text = jobData.location
            binding.txtJobType.text =
                if (jobData.isFulltime) str(R.string.job_type_full)
                else str(R.string.job_type_part)
        }
    }

    private fun setupWidgets() {
        viewPagerSetupHelper.setup(
            binding.tabJob,
            binding.vpTabJob,
            supportFragmentManager,
            pagerAdapter(listOf(JobDescriptionFragment(), JobRequirementFragment())),
            TAB_TITLES
        )
        binding.btnBack.setOnClickListener { onBackPressed() }
    }
}