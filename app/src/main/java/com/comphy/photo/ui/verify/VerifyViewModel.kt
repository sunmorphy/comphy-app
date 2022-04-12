package com.comphy.photo.ui.verify

import com.comphy.photo.base.BaseAuthViewModel
import com.comphy.photo.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    suspend fun userRegisterVerify(otp: String, email: String) {
        authRepository.userRegisterVerify(
            otp,
            email,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }

    suspend fun userForgotVerify(otp: String, email: String) {
        authRepository.userForgotVerify(
            otp,
            email,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }

}