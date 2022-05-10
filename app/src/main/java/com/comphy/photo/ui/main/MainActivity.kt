package com.comphy.photo.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseMainActivity
import com.comphy.photo.databinding.ActivityMainBinding
import com.comphy.photo.ui.main.fragment.job.JobViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseMainActivity() {
    private lateinit var binding: ActivityMainBinding
//    private val mainViewModel: MainViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch { jobViewModel.getRegencies() }

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
        // TODO("Not yet implemented")
    }
}