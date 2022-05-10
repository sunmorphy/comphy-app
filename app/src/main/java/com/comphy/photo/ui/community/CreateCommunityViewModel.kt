package com.comphy.photo.ui.community

import androidx.lifecycle.MutableLiveData
import com.comphy.photo.base.viewmodel.BaseCommunityViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.LocationRepository
import com.comphy.photo.data.repository.UploadRepository
import com.comphy.photo.data.source.local.entity.RegencyEntity
import com.comphy.photo.data.source.remote.response.community.create.CreateCommunityBody
import com.comphy.photo.data.source.remote.response.upload.DataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateCommunityViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val uploadRepository: UploadRepository,
    private val communityRepository: CommunityRepository
) : BaseCommunityViewModel() {

    val regencies = MutableLiveData<List<RegencyEntity>>()
    val uploadsUrl = MutableLiveData<List<DataItem>>()
    val uploadResp = MutableLiveData<String>()

    suspend fun getRegencies() =
        locationRepository.getRegencies()
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { regencies.postValue(it) }

    suspend fun getUploadLink(
        type: String,
        amount: Int
    ) = uploadRepository.getUploadLink(
        type = type,
        amount = amount
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { uploadsUrl.postValue(it.data) }

    suspend fun uploadImageNonPost(
        url: String,
        image: RequestBody
    ) = uploadRepository.uploadImagesNonPost(url, image, onSuccess = {
        uploadResp.postValue("uhh it's a success... i guess")
        println("INI DARI ON SUCCESS")
    })
        .onStart { isLoading.postValue(true) }
        .collect {
            uploadResp.postValue("uhh it's a success... i guess")
            println("uhh it's a success... i guess")
        }

    suspend fun createCommunity(
        communityName: String,
        description: String,
        location: String,
        communityType: String,
        categoryCommunityId: Int,
        profilePhotoCommunityLink: String? = null,
        bannerPhotoCommunityLink: String? = null
    ) = communityRepository.createCommunity(
        createCommunityBody = CreateCommunityBody(
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