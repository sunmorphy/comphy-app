package com.comphy.photo.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.remote.response.community.created.CreatedCommunityResponseContent
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {

    val userData = MutableLiveData<UserResponseData>()
    val userCreatedCommunity = MutableLiveData<List<CreatedCommunityResponseContent>>()
    val userJoinedCommunity = MutableLiveData<List<CreatedCommunityResponseContent>>()
    val isFetching = MutableLiveData<Boolean>()
    val exceptionResponse = MutableLiveData<String>()
    val leaveResponse = MutableLiveData<String>()

    suspend fun getUserDetails() {
        userRepository.getUserDetails()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect {
                if (it != null) {
                    userData.postValue(it)
                }
            }
    }

    suspend fun leaveCommunity(communityId: Int) =
        communityRepository.leaveCommunity(
            communityId = communityId,
            onErrorNorException = { exceptionResponse.postValue(it) }
        )
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { leaveResponse.postValue(it.message) }

    suspend fun getCreatedCommunities() =
        communityRepository.getCreatedCommunities {
            suspend {
                communityRepository.getCreatedCommunities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect { userCreatedCommunity.postValue(it.data?.content!!) }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userCreatedCommunity.postValue(it.data?.content!!) }

    suspend fun getJoinedCommunities() =
        communityRepository.getJoinedCommunities {
            suspend {
                communityRepository.getJoinedCommunities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect { userJoinedCommunity.postValue(it.data?.content!!) }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userJoinedCommunity.postValue(it.data?.content!!) }

}