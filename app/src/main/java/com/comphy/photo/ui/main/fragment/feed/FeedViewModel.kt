package com.comphy.photo.ui.main.fragment.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val feedResponse = MutableLiveData<FeedResponse>()

    suspend fun getFeedPost(page: Int? = null, perPage: Int? = null) =
        postRepository.getFeedPosts(page, perPage)
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { feedResponse.postValue(it) }

}