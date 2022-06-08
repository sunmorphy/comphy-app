package com.comphy.photo.ui.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.comphy.photo.data.repository.EventRepository
import com.comphy.photo.data.repository.JobRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.data.source.remote.response.job.bookmark.BookmarkJobResponseContentItem
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val jobRepository: JobRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val isDeletePostLoading = MutableLiveData<Boolean>()
    val cities = MutableLiveData<List<CityEntity>>()
    val successResponse = MutableLiveData<Int>()
    val eventResponse = MutableLiveData<List<EventResponseContentItem>>()
    val jobResponse = MutableLiveData<List<BookmarkJobResponseContentItem>>()
    val postResponse = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val userData = MutableLiveData<UserResponseData>()
    val errorNorException = MutableLiveData<String>()
    val followResponse = MutableLiveData<String>()
    val deleteResponse = MutableLiveData<Int>()

    suspend fun getBookmarkedEvents() =
        eventRepository.getBookmarkedEvents()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { eventResponse.postValue(it) }

    suspend fun getBookmarkedJobs() =
        jobRepository.getBookmarkedJobs()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { jobResponse.postValue(it) }

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