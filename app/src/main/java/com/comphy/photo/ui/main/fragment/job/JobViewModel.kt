package com.comphy.photo.ui.main.fragment.job

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.JobRepository
import com.comphy.photo.data.repository.LocationRepository
import com.comphy.photo.data.source.local.entity.RegencyEntity
import com.comphy.photo.data.source.remote.response.job.list.JobResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val jobRepository: JobRepository
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val regencies = MutableLiveData<List<RegencyEntity>>()
    val jobResponse = MutableLiveData<JobResponse>()

    suspend fun getRegencies() =
        locationRepository.getRegencies().collect {
            regencies.postValue(it)
        }

    suspend fun getJobs(page: Int? = null, perPage: Int? = null) =
        jobRepository.getJobs(page, perPage)
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { jobResponse.postValue(it) }

    suspend fun getFilteredJobs(
        page: Int? = null,
        perPage: Int? = null,
        region: String,
        isFullTime: Boolean = false,
        isPartTime: Boolean = false
    ) =
        jobRepository.getFilteredJobs(
            page,
            perPage,
            region,
            isFullTime,
            isPartTime
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { jobResponse.postValue(it) }
}