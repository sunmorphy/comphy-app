package com.comphy.photo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseAuthViewModel : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val statusCode = MutableLiveData<Int>()
    val message = MutableLiveData<String>()
}