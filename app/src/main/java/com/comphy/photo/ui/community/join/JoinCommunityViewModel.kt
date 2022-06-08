package com.comphy.photo.ui.community.join

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class JoinCommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val communityResponse = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val exceptionResponse = MutableLiveData<String>()

    suspend fun getCommunityByCategory(categoryId: Int) =
        communityRepository.getFilteredCommunities(categoryId = categoryId) {
            exceptionResponse.postValue(it)
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { communityResponse.postValue(it) }
}