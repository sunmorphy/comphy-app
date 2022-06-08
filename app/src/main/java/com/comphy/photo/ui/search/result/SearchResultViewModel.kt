package com.comphy.photo.ui.search.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.*
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.data.source.remote.response.job.list.JobResponseContentItem
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.data.source.remote.response.user.following.UserFollowingResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val eventRepository: EventRepository,
    private val jobRepository: JobRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val isDeletePostLoading = MutableLiveData<Boolean>()
    val cities = MutableLiveData<List<CityEntity>>()
    val usersResponse = MutableLiveData<List<UserResponseData>>()
    val communitiesResponse = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val postsResponse = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val jobsResponse = MutableLiveData<List<JobResponseContentItem>>()
    val eventResponse = MutableLiveData<List<EventResponseContentItem>>()
    val userData = MutableLiveData<UserResponseData>()
    val errorNorException = MutableLiveData<String>()
    val successResponse = MutableLiveData<Int>()
    val followResponse = MutableLiveData<String>()
    val deleteResponse = MutableLiveData<Int>()

    suspend fun getCities() =
        userRepository.getUserCities {
            suspend {
                userRepository.getUserCities {
                    errorNorException.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect { cities.postValue(it) }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { cities.postValue(it) }

    suspend fun getFilteredUsers(
        name: String? = null,
        location: String? = null
    ) = userRepository.getFilteredUsers(name = name, location = location)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { usersResponse.postValue(it) }

    suspend fun getFilteredCommunities(
        key: String,
        location: String? = null
    ) = communityRepository.getFilteredCommunities(
            communityName = key,
            location = location
        ) { errorNorException.postValue(it) }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { communitiesResponse.postValue(it) }

    suspend fun getFilteredPost(
        key: String,
        location: String? = null
    ) =
        postRepository.getFilteredPosts(titlePost = key, location = location)
            .cachedIn(viewModelScope)
            .collectLatest { postsResponse.postValue(it) }

    // TODO: GET FILTERED JOBS NOT READY YET
//    suspend fun getFilteredJob(key: String) =
//        jobRepository.getFilteredJobs()

    suspend fun getFilteredEvent(key: String) =
        eventRepository.getEventsByName(key)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { eventResponse.postValue(it) }

    suspend fun getUserDetails() {
        userRepository.getUserDetails()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userData.postValue(it) }
    }

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