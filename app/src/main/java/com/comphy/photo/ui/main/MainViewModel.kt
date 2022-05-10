package com.comphy.photo.ui.main
//
//import androidx.lifecycle.ViewModel
//import com.comphy.photo.data.repository.LocationRepository
//import com.comphy.photo.utils.FetchHelper
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.CoroutineDispatcher
//import javax.inject.Inject
//
//@HiltViewModel
//class MainViewModel @Inject constructor(
//    private val locationRepository: LocationRepository,
//    private val ioDispatcher: CoroutineDispatcher
//) : ViewModel() {
//
//    fun cancelFetch() = FetchHelper(locationRepository, ioDispatcher).cancelFetch()
//
//}