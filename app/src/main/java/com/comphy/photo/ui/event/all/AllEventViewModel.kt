package com.comphy.photo.ui.event.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.repository.EventRepository
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class AllEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val eventResponse = MutableLiveData<List<EventResponseContentItem>>()
    val searchEventResponse = MutableLiveData<List<EventResponseContentItem>>()

    suspend fun getEvents() =
        eventRepository.getEvents()
            .collect { eventResponse.postValue(it) }

    suspend fun getEventsByName(name: String) =
        eventRepository.getEventsByName(name)
            .collect { searchEventResponse.postValue(it) }
}