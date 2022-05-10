package com.comphy.photo.ui.biodata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.LocationRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.ProvinceWithRegency
import com.comphy.photo.data.source.local.entity.RegencyEntity
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.auth.AuthResponse
import com.comphy.photo.data.source.remote.response.user.UserData
import com.comphy.photo.data.source.remote.response.user.UserDataBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BiodataViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val userAuth: UserAuth
) : ViewModel() {

    val userData = MutableLiveData<UserData>()
    val isFetching = MutableLiveData(false)
    val locationResponse = MutableLiveData<List<ProvinceWithRegency>>()
    val regencies = MutableLiveData<List<RegencyEntity>>()
    val updateResponse = MutableLiveData<AuthResponse>()

    suspend fun getUserDetails() {
        if (userAuth.userId != 0) {
            userRepository.getUserDetails()
                .onStart { isFetching.postValue(true) }
                .onCompletion { isFetching.postValue(false) }
                .collect {
                    if (it.userData != null) {
                        userData.postValue(it.userData!!)
                    }
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
        userRepository.updateUserDetails(
            userDataBody,
            onError = { },
            onException = { }
        )
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { updateResponse.postValue(it) }
    }

    suspend fun getRegencies() =
        locationRepository.getRegencies().collect {
            regencies.postValue(it)
        }

}