package com.comphy.photo.ui.search.explore.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.comphy.photo.R
import com.comphy.photo.data.source.local.sharedpref.search.SearchHistory
import com.comphy.photo.databinding.ActivityExploreBinding
import com.comphy.photo.helpers.CustomGridSpacingDecoration
import com.comphy.photo.ui.search.explore.detail.ExploreDetailActivity
import com.comphy.photo.ui.search.explore.main.viewholder.SearchHistoryAdapter
import com.comphy.photo.ui.search.result.SearchResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import splitties.activities.start
import javax.inject.Inject

@AndroidEntryPoint
class ExploreActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CONTENT_ITEM = "extra_content_item"
        private const val EXTRA_KEY = "extra_key"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExploreBinding.inflate(layoutInflater)
    }
    private val viewModel: ExploreViewModel by viewModels()
    private var exploreAdapter: ExploreAdapter? = null
    private var latestSearch = ""

    @Inject
    lateinit var searchHistory: SearchHistory

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
        binding.edtSearch.apply {
            setOnFocusChangeListener { _, b ->
                if (b) {
                    binding.txtExplore.text = getString(R.string.string_search)
                    binding.layoutSearchHistory.visibility = View.VISIBLE
                    binding.rvSearchHistory.apply {
                        layoutManager = LinearLayoutManager(this@ExploreActivity)
                        adapter = SearchHistoryAdapter(searchHistory.histories.toList()) {
                            start<SearchResultActivity> { putExtra(EXTRA_KEY, it) }
                        }
                    }
                    binding.rvExplore.visibility = View.GONE
                } else {
                    binding.txtExplore.text = getString(R.string.string_explore)
                    binding.layoutSearchHistory.visibility = View.GONE
                    binding.rvExplore.visibility = View.VISIBLE
                }
            }
            setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    with(searchHistory.histories) {
                        if (size >= 3) {
                            remove(last())
                            add(text.toString())
                        } else {
                            add(text.toString())
                        }
                    }
                    latestSearch = text.toString()
                    start<SearchResultActivity> { putExtra(EXTRA_KEY, text.toString()) }
                }
                false
            }
        }
        binding.root.setOnTouchListener { view, _ ->
            binding.edtSearch.clearFocus()
            return@setOnTouchListener view.performClick()
        }
        lifecycleScope.launch {
            viewModel.explorePhotos().collectLatest { exploreAdapter!!.submitData(it) }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.edtSearch.setText(latestSearch)
    }
}