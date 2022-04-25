package com.comphy.photo.ui.biodata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.LocationRepository
import com.comphy.photo.data.model.entity.ProvinceWithRegency
import com.comphy.photo.data.model.entity.RegencyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class BiodataViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    val isFetching = MutableLiveData(false)
    val locationResponse = MutableLiveData<List<ProvinceWithRegency>>()
    val regencies = MutableLiveData<List<RegencyEntity>>()

    suspend fun fetchLocation() =
        locationRepository.fetchLocation()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect {  }

    suspend fun getRegencies() =
        locationRepository.getRegencies().collect {
            regencies.postValue(it)
        }

}