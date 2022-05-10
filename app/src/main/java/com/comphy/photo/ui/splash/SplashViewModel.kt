package com.comphy.photo.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val isFetching = MutableLiveData(false)

    suspend fun fetchLocation() =
        locationRepository.fetchLocation()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { }

//    fun ffetchLocation() = FetchHelper(locationRepository, ioDispatcher).fetchLocation()

}