package com.comphy.photo.ui.community.create

import androidx.lifecycle.MutableLiveData
import com.comphy.photo.base.viewmodel.BaseCommunityViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.UploadRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.community.category.CategoryCommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.create.CommunityBody
import com.comphy.photo.data.source.remote.response.upload.DataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateCommunityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val uploadRepository: UploadRepository,
    private val communityRepository: CommunityRepository
) : BaseCommunityViewModel() {

    val cities = MutableLiveData<List<CityEntity>>()
    val categories = MutableLiveData<List<CategoryCommunityResponseContentItem>>()
    val uploadsUrl = MutableLiveData<List<DataItem>>()
    val uploadResp = MutableLiveData<String>()
    val isFetching = MutableLiveData<Boolean>()

    suspend fun getCities() =
        userRepository.getUserCities {
            suspend {
                userRepository.getUserCities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
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

    suspend fun getCommunityCategories() =
        communityRepository.getCommunityCategories()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { categories.postValue(it) }

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

    suspend fun createCommunity(
        communityName: String,
        description: String,
        location: String,
        communityType: String,
        categoryCommunityId: Int,
        profilePhotoCommunityLink: String? = null,
        bannerPhotoCommunityLink: String? = null
    ) = communityRepository.createCommunity(
        communityBody = CommunityBody(
            communityType = communityType,
            categoryCommunityId = categoryCommunityId,
            description = description,
            profilePhotoCommunityLink = profilePhotoCommunityLink,
            communityName = communityName,
            location = location,
            bannerPhotoCommunityLink = bannerPhotoCommunityLink
        ),
        onError = { message.postValue(it.message) },
        onException = { exceptionResponse.postValue(it) }
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { communityResponse.postValue(it) }

}