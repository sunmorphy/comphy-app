package com.comphy.photo.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseMainActivity
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.databinding.ActivityMainBinding
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.fragment.feed.FeedViewModel
import com.comphy.photo.ui.main.fragment.home.HomeViewModel
import com.comphy.photo.ui.main.fragment.job.JobViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseMainActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }

    @Inject
    lateinit var userAuth: UserAuth

    private val mainViewModel: MainViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)
    }

    override fun init() {
        // TODO("Not yet implemented")
    }

    override fun setupClickListener() {
        // TODO("Not yet implemented")
    }

    override fun setupObserver() {
        mainViewModel.isLocationFetching.observe(this) { customLoading.showLoading(it) }
        homeViewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        feedViewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        jobViewModel.isLoading.observe(this) { customLoading.showLoading(it) }
    }
}