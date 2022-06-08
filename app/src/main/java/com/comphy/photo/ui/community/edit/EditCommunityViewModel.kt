package com.comphy.photo.ui.community.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.UploadRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.community.create.CommunityBody
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.member.MemberCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.upload.DataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class EditCommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val uploadRepository: UploadRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val isDeleteLoading = MutableLiveData<Boolean>()
    val isBanLoading = MutableLiveData<Boolean>()
    val communityMembers = MutableLiveData<List<MemberCommunityResponseContentItem>>()
    val detailCommunity = MutableLiveData<FollowCommunityResponseContentItem>()
    val cities = MutableLiveData<List<CityEntity>>()
    val uploadsUrl = MutableLiveData<List<DataItem>>()
    val uploadResp = MutableLiveData<String>()
    val successResponse = MutableLiveData<String>()
    val banResponse = MutableLiveData<String>()
    val errorNorExceptionResponse = MutableLiveData<String>()

    suspend fun getCities() =
        userRepository.getUserCities {
            suspend {
                userRepository.getUserCities {
                    errorNorExceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect {
                        isFetching.postValue(false)
                        cities.postValue(it)
                    }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { cities.postValue(it) }

    suspend fun getAdminCommunityDetails(communityId: Int) =
        communityRepository.getAdminCommunityDetails(communityId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { detailCommunity.postValue(it) }

    suspend fun getUploadLink(
        type: String,
        amount: Int
    ) = uploadRepository.getUploadLink(
        type = type,
        amount = amount
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { uploadsUrl.postValue(it.data!!) }

    suspend fun uploadImageNonPost(
        url: String,
        image: RequestBody
    ) = uploadRepository.uploadImagesNonPost(url, image)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { uploadResp.postValue("Sukses mengunggah foto") }

    suspend fun editCommunity(
        communityId: Int,
        communityName: String,
        location: String,
        description: String,
        communityType: String,
        profilePhotoCommunityLink: String? = null,
        bannerPhotoCommunityLink: String? = null,
        categoryCommunityId: Int
    ) {
        val communityBody = CommunityBody(
            id = communityId,
            communityName = communityName,
            location = location,
            description = description,
            communityType = communityType,
            profilePhotoCommunityLink = profilePhotoCommunityLink,
            bannerPhotoCommunityLink = bannerPhotoCommunityLink,
            categoryCommunityId = categoryCommunityId
        )
        communityRepository.editCommunity(communityBody) {
            errorNorExceptionResponse.postValue(it)
        }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }
    }

    suspend fun deleteCommunity(communityId: Int) =
        communityRepository.deleteCommunity(communityId) {
            errorNorExceptionResponse.postValue(it)
        }
            .onStart { isDeleteLoading.postValue(true) }
            .onCompletion { isDeleteLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

    suspend fun editPrivateCommunityCode(communityId: Int) =
        communityRepository.editPrivateCommunityCode(communityId) {
            errorNorExceptionResponse.postValue(it)
        }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }

    suspend fun getCommunityMembers(communityId: Int) =
        communityRepository.getCommunityMembers(communityId) {
            errorNorExceptionResponse.postValue(it)
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { communityMembers.postValue(it) }

    suspend fun banUserCommunity(
        userId: Int,
        communityId: Int,
        userName: String
    ) = communityRepository.banUserCommunity(userId, communityId) {
        errorNorExceptionResponse.postValue(it)
    }
        .onStart { isBanLoading.postValue(true) }
        .onCompletion { isBanLoading.postValue(false) }
        .collect { banResponse.postValue(userName) }

}