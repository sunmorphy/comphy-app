package com.comphy.photo.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.data.source.remote.response.BaseMessageResponse

abstract class BaseCommunityViewModel : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val communityResponse = MutableLiveData<BaseMessageResponse>()
    val exceptionResponse = MutableLiveData<String?>()
}