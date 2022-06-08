package com.comphy.photo.ui.biodata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.BaseResponse
import com.comphy.photo.data.source.remote.response.user.detail.UserDataBody
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BiodataViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userAuth: UserAuth
) : ViewModel() {

    val userData = MutableLiveData<UserResponseData>()
    val isFetching = MutableLiveData<Boolean>()
    val exceptionResponse = MutableLiveData<String>()
    val cities = MutableLiveData<List<CityEntity>>()
    val updateResponse = MutableLiveData<String>()

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

    suspend fun getUserDetails() {
        if (userAuth.userId != 0) {
            userRepository.getUserDetails()
                .onStart { isFetching.postValue(true) }
                .onCompletion { isFetching.postValue(false) }
                .collect {
                    if (it!!.location != null
                        && it.job != null
                        && it.description != null
                    ) {
                        userAuth.isUserUpdated = true
                    }
                    userData.postValue(it)
                }
        }
    }

    suspend fun updateUserDetails(
        fullname: String,
        location: String,
        numberPhone: String?,
        job: String,
        description: String,
        socialMedia: String?
    ) {
        val userDataBody = UserDataBody(
            fullname = fullname,
            location = location,
            numberPhone = numberPhone,
            job = job,
            description = description,
            socialMedia = socialMedia,
            id = userAuth.userId
        )
        userRepository.updateUserDetails(userDataBody) {
            exceptionResponse.postValue(it)
        }
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect {
                userAuth.isUserUpdated = true
                updateResponse.postValue(it.message)
            }
    }

}