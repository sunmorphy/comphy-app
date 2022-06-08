package com.comphy.photo.ui.main.fragment.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.EventRepository
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val onErrorNorException = MutableLiveData<String>()
    val eventResponse = MutableLiveData<List<EventResponseContentItem>>()
    val successResponse = MutableLiveData<Int>()

    suspend fun getEvents() =
        eventRepository.getEvents()
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { eventResponse.postValue(it) }

    suspend fun getEventsByName(name: String) =
        eventRepository.getEventsByName(name)
            .onStart { isFetching.postValue(true) }
            .onCompletion { isFetching.postValue(false) }
            .collect { eventResponse.postValue(it) }

    suspend fun bookmarkEvent(eventId: Int, position: Int) =
        eventRepository.bookmarkEvent(eventId) {
            onErrorNorException.postValue(it)
        }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }

    suspend fun unbookmarkEvent(bookmarkId: Int, position: Int) =
        eventRepository.unbookmarkEvent(bookmarkId) {
            onErrorNorException.postValue(it)
        }
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { successResponse.postValue(position) }
}