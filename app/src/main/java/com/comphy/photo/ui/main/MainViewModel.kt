package com.comphy.photo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val isLocationFetching = MutableLiveData<Boolean>()
    val cities = MutableLiveData<List<CityEntity>>()
    val exceptionResponse = MutableLiveData<String>()

    suspend fun getCities() =
        userRepository.getUserCities {
            suspend {
                userRepository.getUserCities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isLocationFetching.postValue(true) }
                    .onCompletion { isLocationFetching.postValue(false) }
                    .collect {
                        isLocationFetching.postValue(false)
                        cities.postValue(it)
                    }
            }
        }
            .onStart { isLocationFetching.postValue(true) }
            .onCompletion { isLocationFetching.postValue(false) }
            .collect {
                isLocationFetching.postValue(false)
                cities.postValue(it)
            }

}