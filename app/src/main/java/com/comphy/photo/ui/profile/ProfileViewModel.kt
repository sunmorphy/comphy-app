package com.comphy.photo.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UploadRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.data.source.remote.response.user.following.UserFollowResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository,
    private val postRepository: PostRepository,
    private val uploadRepository: UploadRepository
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isFetching = MutableLiveData<Boolean>()
    val userData = MutableLiveData<UserResponseData>()
    val userFollowing = MutableLiveData<List<UserFollowResponseContentItem>>()
    val userFollowers = MutableLiveData<List<UserFollowResponseContentItem>>()

    suspend fun getUserDetails() =
        userRepository.getUserDetails()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userData.postValue(it) }

    suspend fun getUserFollowing() =
        userRepository.getUserFollowing()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userFollowing.postValue(it) }

    suspend fun getUserFollowers() =
        userRepository.getUserFollowing()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userFollowers.postValue(it) }
}