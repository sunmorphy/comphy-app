package com.comphy.photo.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.CommunityRepository
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.repository.UploadRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.post.create.PostBody
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.data.source.remote.response.upload.DataItem
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.data.source.remote.response.user.experience.ExperienceBody
import com.comphy.photo.data.source.remote.response.user.follower.UserFollowersResponseContentItem
import com.comphy.photo.data.source.remote.response.user.following.UserFollowingResponseContentItem
import com.comphy.photo.vo.CommunityImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository,
    private val postRepository: PostRepository,
    private val uploadRepository: UploadRepository,
    private val userAuth: UserAuth
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val isUpdateUserDataLoading = MutableLiveData<Boolean>()
    val isDeletePostLoading = MutableLiveData<Boolean>()
    val isChangePasswordLoading = MutableLiveData<Boolean>()
    val isFetching = MutableLiveData<Boolean>()
    val cities = MutableLiveData<List<CityEntity>>()
    val userData = MutableLiveData<UserResponseData>()
    val userPostsCount = MutableLiveData<Int>()
    val userFollowing = MutableLiveData<List<UserFollowingResponseContentItem>>()
    val userFollowers = MutableLiveData<List<UserFollowersResponseContentItem>>()
    val errorNorException = MutableLiveData<String>()
    val updateResponse = MutableLiveData<String>()
    val successResponse = MutableLiveData<Int>()
    val successVerifyPasswordResponse = MutableLiveData<String>()
    val errorChangePasswordResponse = MutableLiveData<String>()
    val successChangePasswordResponse = MutableLiveData<String>()
    val followResponse = MutableLiveData<String>()
    val followPositionResponse = MutableLiveData<Int>()
    val deleteResponse = MutableLiveData<Int>()
    val experienceResponse = MutableLiveData<String>()
    val updateExperienceResponse = MutableLiveData<Int>()
    val filteredPostsResponse = MutableLiveData<PagingData<FeedResponseContentItem>>()
    val uploadsUrl = MutableLiveData<List<DataItem>>()
    val uploadFor = MutableLiveData<CommunityImageType?>()
    val uploadResp = MutableLiveData<String>()

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

    suspend fun getFilteredPost(userId: Int) =
        postRepository.getFilteredPosts(userId = userId)
            .cachedIn(viewModelScope)
            .collectLatest { filteredPostsResponse.postValue(it) }

    suspend fun getUserDetails() =
        userRepository.getUserDetails()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userData.postValue(it) }

    suspend fun getUserDetailsById(userId: Int) =
        userRepository.getUserDetailsById(userId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userData.postValue(it) }

    suspend fun getUserFollowing(userId: Int? = null) =
        userRepository.getUserFollowing(userId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userFollowing.postValue(it) }

    suspend fun getUserFollowers(userId: Int? = null) =
        userRepository.getUserFollowers(userId)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userFollowers.postValue(it) }

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

    suspend fun updateUserDetails(
        fullname: String,
        location: String?,
        numberPhone: String?,
        job: String?,
        description: String?,
        socialMedia: String?,
        profilePhotoLink: String?,
        profileBannerLink: String?
    ) {
        val userDataBody = UserDataBody(
            fullname = fullname,
            location = location,
            numberPhone = numberPhone,
            job = job,
            description = description,
            socialMedia = socialMedia,
            id = userAuth.userId,
            profilePhotoLink = profilePhotoLink,
            profileBannerLink = profileBannerLink
        )
        userRepository.updateUserDetails(userDataBody) {
            errorNorException.postValue(it)
        }
            .onStart { isUpdateUserDataLoading.postValue(true) }
            .onCompletion { isUpdateUserDataLoading.postValue(false) }
            .collect { updateResponse.postValue(it.message) }
    }

    suspend fun followUser(userIdFollowed: Int, position: Int? = null) =
        userRepository.followUser(userIdFollowed) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                followResponse.postValue(it.message)
                if (position != null) {
                    followPositionResponse.postValue(position!!)
                }
            }

    suspend fun unfollowUser(userIdFollowed: Int, position: Int? = null) =
        userRepository.unfollowUser(userIdFollowed) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                followResponse.postValue(it.message)
                if (position != null) {
                    followPositionResponse.postValue(position!!)
                }
            }

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

    suspend fun createExperience(
        company: String,
        position: String,
        years: String
    ) {
        val experienceBody = ExperienceBody(
            company = company,
            position = position,
            years = years
        )
        userRepository.createExperience(experienceBody) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { experienceResponse.postValue(it.message) }
    }

    suspend fun updateExperience(
        experienceId: Int,
        company: String,
        position: String,
        years: String,
        adapterPosition: Int
    ) {
        val experienceBody = ExperienceBody(
            id = experienceId,
            company = company,
            position = position,
            years = years
        )
        userRepository.updateExperience(experienceBody) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { updateExperienceResponse.postValue(adapterPosition) }
    }

    suspend fun deleteExperience(experienceId: Int) =
        userRepository.deleteExperience(experienceId) { errorNorException.postValue(it) }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { experienceResponse.postValue(it.message) }

    suspend fun verifyPassword(password: String) =
        userRepository.verifyPassword(password)
            .onStart { isChangePasswordLoading.postValue(true) }
            .onCompletion { isChangePasswordLoading.postValue(false) }
            .collect { successVerifyPasswordResponse.postValue(it.message) }

    suspend fun changePassword(newPassword: String) =
        userRepository.changePassword(newPassword) {
            errorChangePasswordResponse.postValue(it)
        }
            .onStart { isChangePasswordLoading.postValue(true) }
            .onCompletion { isChangePasswordLoading.postValue(false) }
            .collect { successChangePasswordResponse.postValue(it.message) }

    suspend fun getUploadLink(
        type: String,
        amount: Int,
        imageFor: CommunityImageType? = null
    ) = uploadRepository.getUploadLink(
        type = type,
        amount = amount
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect {
            uploadsUrl.postValue(it.data!!)
            uploadFor.postValue(imageFor)
        }

    suspend fun uploadImageNonPost(
        url: String,
        image: RequestBody
    ) = uploadRepository.uploadImagesNonPost(url, image)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { uploadResp.postValue("Sukses mengunggah foto") }
}