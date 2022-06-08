package com.comphy.photo.ui.main.fragment.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {

    val userData = MutableLiveData<UserResponseData>()
    val isLoading = MutableLiveData<Boolean>()
    val isDeletePostLoading = MutableLiveData<Boolean>()
    val isFetching = MutableLiveData<Boolean>()
    val isFeedFetching = MutableLiveData<Boolean>()
    val categories = MutableLiveData<List<CategoryCommunityResponseContentItem>>()
    val filterResponse = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val errorNorException = MutableLiveData<String>()
    val successResponse = MutableLiveData<Int>()
    val followResponse = MutableLiveData<String>()
    val deleteResponse = MutableLiveData<Int>()
    val feedResponse = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val feedsResponse = MutableLiveData<List<FeedResponseContentItem>>()

    suspend fun getFeedPost() =
        postRepository.getFeedPosts()
            .cachedIn(viewModelScope)
            .collectLatest { feedResponse.postValue(it) }

    suspend fun getFeeds() =
        postRepository.getFeeds()
            .onStart { isFeedFetching.postValue(true) }
            .onCompletion { isFeedFetching.postValue(false) }
            .collect { feedsResponse.postValue(it) }

    suspend fun getFilteredPost(categoryId: Int? = null) =
        postRepository.getFilteredPosts(categoryId = categoryId)
            .cachedIn(viewModelScope)
            .collectLatest { filterResponse.postValue(it) }

    suspend fun getCommunityCategories() =
        communityRepository.getCommunityCategories()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { categories.postValue(it) }

    suspend fun updatePost(
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