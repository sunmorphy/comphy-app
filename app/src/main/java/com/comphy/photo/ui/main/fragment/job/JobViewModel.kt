package com.comphy.photo.ui.main.fragment.job

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.JobRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.job.list.JobResponse
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val jobResponse = MutableLiveData<JobResponse>()
    val cities = MutableLiveData<List<CityEntity>>()
    val exceptionResponse = MutableLiveData<String>()

    suspend fun getCities() =
        userRepository.getUserCities {
            suspend {
                userRepository.getUserCities {
                    exceptionResponse.postValue("Mohon cek koneksi internet anda")
                }
                    .onStart { isLoading.postValue(true) }
                    .onCompletion { isLoading.postValue(false) }
                    .collect { cities.postValue(it) }
            }
        }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { cities.postValue(it) }

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