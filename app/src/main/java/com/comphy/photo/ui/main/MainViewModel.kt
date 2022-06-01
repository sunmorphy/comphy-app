package com.comphy.photo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userData = MutableLiveData<UserResponseData>()
    val isFetching = MutableLiveData<Boolean>()
    val isLocationFetching = MutableLiveData<Boolean>()
    val cities = MutableLiveData<List<CityEntity>>()
    val exceptionResponse = MutableLiveData<String>()

    suspend fun getUserDetails() {
        userRepository.getUserDetails()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { userData.postValue(it) }
    }

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

}