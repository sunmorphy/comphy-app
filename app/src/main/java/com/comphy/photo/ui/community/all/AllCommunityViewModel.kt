package com.comphy.photo.ui.community.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.source.remote.response.community.created.CreatedCommunityResponseContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class AllCommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {

    val userCreatedCommunity = MutableLiveData<List<CreatedCommunityResponseContent>>()
    val userJoinedCommunity = MutableLiveData<List<CreatedCommunityResponseContent>>()
    val isFetching = MutableLiveData<Boolean>()
    val exceptionResponse = MutableLiveData<String>()
    val leaveResponse = MutableLiveData<String>()

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