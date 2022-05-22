package com.comphy.photo.ui.search.explore.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comphy.photo.databinding.ActivityExploreDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreDetailActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExploreDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}