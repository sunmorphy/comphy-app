package com.comphy.photo.ui.main.fragment.job

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.JobRepository
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.data.source.remote.response.job.bookmark.BookmarkJobResponseContentItem
import com.comphy.photo.data.source.remote.response.job.list.JobResponseContentItem
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
    val jobResponse = MutableLiveData<List<JobResponseContentItem>>()
    val filteredJobResponse = MutableLiveData<List<JobResponseContentItem>>()
    val bookmarkedJobResponse = MutableLiveData<List<BookmarkJobResponseContentItem>>()
    val bookmarkedJobIdResponse = MutableLiveData<Int>()
    val bookmarkedJobPositionResponse = MutableLiveData<Int>()
    val cities = MutableLiveData<List<CityEntity>>()
    val exceptionResponse = MutableLiveData<String>()
    val bookmarkSuccessResponse = MutableLiveData<Int>()
    val unbookmarkSuccessResponse = MutableLiveData<Int>()

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
        title: String? = null,
        location: String? = null
    ) = jobRepository.getFilteredJobs(page, perPage, title, location)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { filteredJobResponse.postValue(it) }

    suspend fun getBookmarkedJobs(
        page: Int? = null,
        perPage: Int? = null,
        jobId: Int,
        position: Int
    ) = jobRepository.getBookmarkedJobs(page, perPage)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect {
            bookmarkedJobResponse.postValue(it)
            bookmarkedJobIdResponse.postValue(jobId)
            bookmarkedJobPositionResponse.postValue(position)
        }

    suspend fun bookmarkJob(
        jobId: Int,
        position: Int
    ) = jobRepository.bookmarkJob(jobId)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { bookmarkSuccessResponse.postValue(position) }

    suspend fun unbookmarkJob(
        jobId: Int,
        position: Int
    ) = jobRepository.unBookmarkJob(jobId)
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { unbookmarkSuccessResponse.postValue(position) }
}