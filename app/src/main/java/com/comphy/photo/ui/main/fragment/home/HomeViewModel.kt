package com.comphy.photo.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.EventRepository
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    val userData = MutableLiveData<UserResponseData>()
    val userCreatedCommunity = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val userJoinedCommunity = MutableLiveData<List<FollowCommunityResponseContentItem>>()
    val events = MutableLiveData<List<EventResponseContentItem>>()
    val isLoading = MutableLiveData<Boolean>()
    val isFetching = MutableLiveData<Boolean>()
    val exceptionResponse = MutableLiveData<String>()
    val leaveResponse = MutableLiveData<String>()

    suspend fun leaveCommunity(communityId: Int) =
        communityRepository.leaveCommunity(
            communityId = communityId,
            onErrorNorException = { exceptionResponse.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { leaveResponse.postValue(it.message) }

    suspend fun getEvents() =
        eventRepository.getEvents()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { events.postValue(it) }

    suspend fun getCreatedCommunities() =
        communityRepository.getCreatedCommunities {
            suspend {
                communityRepository.getCreatedCommunities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect { userCreatedCommunity.postValue(it) }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userCreatedCommunity.postValue(it) }

    suspend fun getJoinedCommunities() =
        communityRepository.getJoinedCommunities {
            suspend {
                communityRepository.getJoinedCommunities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect { userJoinedCommunity.postValue(it) }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userJoinedCommunity.postValue(it) }

}