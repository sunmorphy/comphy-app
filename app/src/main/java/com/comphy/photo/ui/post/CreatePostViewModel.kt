package com.comphy.photo.ui.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UploadRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.community.category.CommunityResponseContentItem
import com.comphy.photo.data.source.remote.response.community.created.CreatedCommunityResponseContent
import com.comphy.photo.data.source.remote.response.post.create.CreatePostBody
import com.comphy.photo.data.source.remote.response.upload.DataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val uploadRepository: UploadRepository,
    private val postRepository: PostRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {

    val cities = MutableLiveData<List<CityEntity>>()
    val categories = MutableLiveData<List<CommunityResponseContentItem>>()
    val uploadsUrl = MutableLiveData<List<DataItem>>()
    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val exceptionResponse = MutableLiveData<String?>()
    val successResponse = MutableLiveData<String>()
    val userCreatedCommunity = MutableLiveData<List<CreatedCommunityResponseContent>>()
    val userJoinedCommunity = MutableLiveData<List<CreatedCommunityResponseContent>>()

    suspend fun getCities() =
        userRepository.getUserCities {
            suspend {
                userRepository.getUserCities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isFetching.postValue(true) }
                    .onCompletion { isFetching.postValue(false) }
                    .collect { cities.postValue(it) }
            }
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { cities.postValue(it) }

    suspend fun getCommunityCategories() =
        communityRepository.getCommunityCategories()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { categories.postValue(it.data!!.content) }

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

    suspend fun getUploadLink(
        type: String,
        amount: Int
    ) = uploadRepository.getUploadLink(
        type = type,
        isPost = true,
        amount = amount
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { uploadsUrl.postValue(it.data!!) }

    suspend fun uploadImagePost(
        url: String,
        image: RequestBody
    ) = uploadRepository.uploadImagesPost(url, image)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { }

    suspend fun uploadVideoPost(
        url: String,
        video: RequestBody
    ) = uploadRepository.uploadVideosPost(url, video)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { }

    suspend fun createPost(
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
        linkVideo: String? = null
    ) {
        val createPostBody = CreatePostBody(
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

        postRepository.createPost(createPostBody)
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(it.message) }
    }

}