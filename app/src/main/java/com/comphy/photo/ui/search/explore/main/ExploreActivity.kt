package com.comphy.photo.ui.search.explore.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.comphy.photo.databinding.ActivityExploreBinding
import com.comphy.photo.helpers.CustomGridSpacingDecoration
import com.comphy.photo.ui.search.explore.detail.ExploreDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class ExploreActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExploreBinding.inflate(layoutInflater)
    }
    private val viewModel: ExploreViewModel by viewModels()
    private var exploreAdapter: ExploreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupWidgets()
    }

    private fun setupWidgets() {
        binding.layoutSearchHistory.visibility = View.GONE
        exploreAdapter = ExploreAdapter {
            start<ExploreDetailActivity> {
                putExtra(EXTRA_CONTENT_ITEM, it)
            }
        }
        binding.rvExplore.apply {
            visibility = View.VISIBLE
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(CustomGridSpacingDecoration(18, 12))
            adapter = exploreAdapter
        }
        lifecycleScope.launch {
            viewModel.explorePhotos().collectLatest { exploreAdapter!!.submitData(it) }
        }
    }
}