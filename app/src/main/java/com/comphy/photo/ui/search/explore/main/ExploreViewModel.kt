package com.comphy.photo.ui.search.explore.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    fun explorePhotos() =
        postRepository.getFilteredPosts(showPhotos = true)
            .cachedIn(viewModelScope)

}