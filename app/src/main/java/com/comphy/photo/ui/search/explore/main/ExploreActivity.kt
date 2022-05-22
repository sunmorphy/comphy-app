package com.comphy.photo.ui.search.explore.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.comphy.photo.data.model.ExploreTestModel
import com.comphy.photo.databinding.ActivityExploreBinding
import com.comphy.photo.helpers.CustomGridSpacingDecoration
import com.comphy.photo.vo.OrientationType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityExploreBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listMedia = mutableListOf<ExploreTestModel>()
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/people/grayscale", OrientationType.SQUARE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/tech/grayscale", OrientationType.LANDSCAPE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/people/grayscale", OrientationType.SQUARE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/nature", OrientationType.PORTRAIT))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/people/grayscale", OrientationType.SQUARE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/tech/grayscale", OrientationType.LANDSCAPE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/tech/grayscale", OrientationType.LANDSCAPE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/people/grayscale", OrientationType.SQUARE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/nature", OrientationType.PORTRAIT))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/tech/grayscale", OrientationType.LANDSCAPE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/tech/grayscale", OrientationType.LANDSCAPE))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/nature", OrientationType.PORTRAIT))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/nature", OrientationType.PORTRAIT))
        listMedia.add(ExploreTestModel("https://placeimg.com/640/480/tech/grayscale", OrientationType.LANDSCAPE))

        binding.layoutSearchHistory.visibility = View.GONE
        binding.rvExplore.apply {
            visibility = View.VISIBLE
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(CustomGridSpacingDecoration(18, 12))
            adapter = ExploreAdapter(listMedia)
        }
    }
}