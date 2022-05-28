package com.comphy.photo.ui.main.fragment.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val userData = MutableLiveData<UserResponseData>()
    val isLoading = MutableLiveData<Boolean>()
    val feedResponse = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val errorNorException = MutableLiveData<String>()
    val successResponse = MutableLiveData<String>()

    suspend fun getUserDetails() {
        userRepository.getUserDetails()
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { userData.postValue(it) }
    }

    suspend fun getFeedPost() =
        postRepository.getFeedPosts()
            .cachedIn(viewModelScope)
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                feedResponse.postValue(it)
                isLoading.postValue(false)
            }
    
    suspend fun likePost(postId: String) =
        postRepository.likePost(postId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

    suspend fun unlikePost(postId: String) =
        postRepository.unlikePost(postId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }
}