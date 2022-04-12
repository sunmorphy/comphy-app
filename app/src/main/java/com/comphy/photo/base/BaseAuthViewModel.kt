package com.comphy.photo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseAuthViewModel : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val authResponse = MutableLiveData<String>()
}