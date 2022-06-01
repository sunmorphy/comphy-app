package com.comphy.photo.ui.community.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.member.MemberCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val onErrorNorException = MutableLiveData<String>()
    val onSuccess = MutableLiveData<String>()
    val createdCommunity = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val similarCommunity = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val detailCommunity = MutableLiveData<FollowCommunityResponseContentItem>()
    val communityMembers = MutableLiveData<List<MemberCommunityResponseContentItem>>()
    val communityPosts = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val communityPhotos = MutableLiveData<PagingData<FeedResponseContentItem>>()

    suspend fun getCreatedCommunity() =
        communityRepository.getCreatedCommunities {
            onErrorNorException.postValue(it)
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { createdCommunity.postValue(it) }

    suspend fun getAdminCommunityDetails(communityId: Int) =
        communityRepository.getAdminCommunityDetails(communityId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { detailCommunity.postValue(it) }

    suspend fun getCommunityDetails(communityId: Int) =
        communityRepository.getCommunityDetails(communityId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { detailCommunity.postValue(it) }

    suspend fun getSimilarCommunity(categoryId: Int) =
        communityRepository.getFilteredCommunities(categoryId = categoryId) {
            onErrorNorException.postValue(it)
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { similarCommunity.postValue(it) }

    suspend fun getCommunityMembers(communityId: Int) =
        communityRepository.getCommunityMembers(communityId) {
            onErrorNorException.postValue(it)
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { communityMembers.postValue(it) }

    suspend fun getCommunityPosts(communityId: Int) =
        postRepository.getFilteredPosts(communityId = communityId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { communityPosts.postValue(it) }

    suspend fun getCommunityPhotos(communityId: Int) =
        postRepository.getFilteredPosts(communityId = communityId, showPhotos = true)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { communityPhotos.postValue(it) }

    suspend fun joinCommunity(
        communityId: Int,
        communityCode: String? = null
    ) = communityRepository.joinCommunity(communityId, communityCode) {
        onErrorNorException.postValue(it)
    }
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { onSuccess.postValue(it.message) }

    suspend fun leaveCommunity(
        communityId: Int
    ) = communityRepository.leaveCommunity(communityId) {
        onErrorNorException.postValue(it)
    }
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { onSuccess.postValue(it.message) }

}