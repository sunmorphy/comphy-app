package com.comphy.photo.ui.community.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.member.MemberCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val isDeletePostLoading = MutableLiveData<Boolean>()
    val onErrorNorException = MutableLiveData<String>()
    val onSuccess = MutableLiveData<String>()
    val userData = MutableLiveData<UserResponseData>()
    val createdCommunity = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val similarCommunity = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val detailCommunity = MutableLiveData<FollowCommunityResponseContentItem>()
    val communityMembers = MutableLiveData<List<MemberCommunityResponseContentItem>>()
    val communityPosts = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val communityPhotos = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val categories = MutableLiveData<List<CategoryCommunityResponseContentItem>>()
    val errorNorException = MutableLiveData<String>()
    val successResponse = MutableLiveData<Int>()
    val followResponse = MutableLiveData<String>()
    val deleteResponse = MutableLiveData<Int>()

    suspend fun getUserDetails() {
        userRepository.getUserDetails()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userData.postValue(it) }
    }

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
            .cachedIn(viewModelScope)
            .collectLatest { communityPosts.postValue(it) }

    suspend fun getCommunityPhotos(communityId: Int) =
        postRepository.getFilteredPosts(communityId = communityId, showPhotos = true)
            .cachedIn(viewModelScope)
            .collectLatest { communityPhotos.postValue(it) }

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

    suspend fun getCommunityCategories() =
        communityRepository.getCommunityCategories()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { categories.postValue(it) }

    suspend fun updatePost(
        postId: String,
        title: String,
        description: String? = null,
        orientationType: Int? = null,
        iso: String? = null,
        lens: String? = null,
        shutterSpeed: String? = null,
        aperture: String? = null,
        camera: String? = null,
        flash: String? = null,
        location: String? = null,
        categoryCommunityId: Int,
        communityId: Int? = null,
        linkPhoto: String? = null,
        linkVideo: String? = null,
        position: Int
    ) {
        val updatePostBody = PostBody(
            postId = postId,
            title = title,
            description = description,
            camera = camera,
            iso = iso,
            lens = lens,
            shutterSpeed = shutterSpeed,
            flash = flash,
            aperture = aperture,
            location = location,
            categoryId = categoryCommunityId,
            communityId = communityId,
            linkPhoto = linkPhoto,
            linkVideo = linkVideo,
            orientationType = orientationType
        )

        postRepository.updatePost(updatePostBody) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }
    }

    suspend fun deletePost(postId: String, position: Int) =
        postRepository.deletePost(postId) { errorNorException.postValue(it) }
            .onStart { isDeletePostLoading.postValue(true) }
            .onCompletion { isDeletePostLoading.postValue(false) }
            .collect { deleteResponse.postValue(position) }

    suspend fun followUser(userIdFollowed: Int) =
        userRepository.followUser(userIdFollowed) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { followResponse.postValue(it.message) }

    suspend fun unfollowUser(userIdFollowed: Int) =
        userRepository.unfollowUser(userIdFollowed) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { followResponse.postValue(it.message) }

    suspend fun bookmarkUser(postId: String, position: Int) =
        postRepository.bookmarkPost(postId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }

    suspend fun unbookmarkUser(savedPostId: String, position: Int) =
        postRepository.unbookmarkPost(savedPostId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }

    suspend fun likePost(postId: String, position: Int) =
        postRepository.likePost(postId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }

    suspend fun unlikePost(postId: String, position: Int) =
        postRepository.unlikePost(postId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }

}