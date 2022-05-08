package com.comphy.photo.ui.biodata

import androidx.lifecycle.MutableLiveData
import com.comphy.photo.base.viewmodel.BaseAuthViewModel
import com.comphy.photo.data.BiodataRepository
import com.comphy.photo.data.LocationRepository
import com.comphy.photo.data.model.entity.ProvinceWithRegency
import com.comphy.photo.data.model.entity.RegencyEntity
import com.comphy.photo.data.model.response.biodata.BiodataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BiodataViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val biodataRepository: BiodataRepository
) : BaseAuthViewModel() {

    val isFetching = MutableLiveData(false)
    val locationResponse = MutableLiveData<List<ProvinceWithRegency>>()
    val regencies = MutableLiveData<List<RegencyEntity>>()
    val biodataResponse = MutableLiveData<BiodataResponse>()

    suspend fun fetchLocation() =
        locationRepository.fetchLocation()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { }

    suspend fun getRegencies() =
        locationRepository.getRegencies().collect {
            regencies.postValue(it)
        }

    suspend fun updateDataUser(
        fullName: String,
        username: String,
        location: String,
        numberPhone: String? = null,
        job: String,
        description: String,
        socialMedia: String,
        id: Int
    ) {

        biodataRepository.updateUserData(
            fullName,
            username,
            location,
            numberPhone,
            job,
            description,
            socialMedia,
            id,
            onError = { message.postValue(it.message) },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { biodataResponse.postValue(it)}
    }
}