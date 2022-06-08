package com.comphy.photo.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseMainActivity
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.databinding.ActivityMainBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.fragment.feed.FeedViewModel
import com.comphy.photo.ui.main.fragment.home.HomeViewModel
import com.comphy.photo.ui.main.fragment.job.JobViewModel
import com.comphy.photo.ui.main.fragment.training.TrainingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseMainActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }

    @Inject
    lateinit var userAuth: UserAuth

    private val viewModel: MainViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()
    private val trainingViewModel: TrainingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getUserDetails()
            viewModel.getCities()
            if (userAuth.userAccessToken == null) {
                start<LoginActivity>()
                finishAffinity()
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
    }

    override fun init() {}

    override fun setupClickListener() {}

    override fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        trainingViewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        homeViewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        homeViewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        feedViewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        jobViewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        trainingViewModel.isLoading.observe(this) { customLoading.showLoading(it) }
    }
}