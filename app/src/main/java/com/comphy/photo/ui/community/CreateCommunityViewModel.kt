package com.comphy.photo.ui.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.LocationRepository
import com.comphy.photo.data.model.entity.RegencyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCommunityViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    val regencies = MutableLiveData<List<RegencyEntity>>()

    suspend fun getRegencies() =
        locationRepository.getRegencies().collect {
            regencies.postValue(it)
        }

}