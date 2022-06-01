package com.comphy.photo.ui.job

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.JobRepository
import com.comphy.photo.data.source.remote.response.job.detail.JobDetailResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val jobDetailResponse = MutableLiveData<JobDetailResponseData>()

    suspend fun getJobDetails(jobId: Int) =
        jobRepository.getJobDetails(jobId)
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { jobDetailResponse.postValue(it) }

}