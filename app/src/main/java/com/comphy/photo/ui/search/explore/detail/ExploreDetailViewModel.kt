package com.comphy.photo.ui.search.explore.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ExploreDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel(){

    val isLoading = MutableLiveData<Boolean>()
    val errorNorException = MutableLiveData<String>()
    val successResponse = MutableLiveData<String>()

    suspend fun followUser(userIdFollowed: Int) =
        userRepository.followUser(userIdFollowed) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

    suspend fun unfollowUser(userIdFollowed: Int) =
        userRepository.unfollowUser(userIdFollowed) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

    suspend fun bookmarkPost(postId: String) =
        postRepository.bookmarkPost(postId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

    suspend fun unbookmarkPost(savedPostId: String) =
        postRepository.unbookmarkPost(savedPostId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

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