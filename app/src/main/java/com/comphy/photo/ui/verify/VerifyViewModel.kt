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

    suspend fun userRegisterVerify(otp: String, email: String, onComplete: () -> Unit) {
        authRepository.userForgotVerify(otp, email,
            responseStatus = { statusCode.postValue(it) },
            responseMessage = { message.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion {
                isLoading.postValue(false)
                onComplete()
            }
            .collect { message.postValue(it.message) }
    }
    
    suspend fun userForgotVerify(otp: String, email: String, onComplete: () -> Unit) {
        authRepository.userForgotVerify(otp, email,
            responseStatus = { statusCode.postValue(it) },
            responseMessage = { message.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion {
                isLoading.postValue(false)
                onComplete()
            }
            .collect { message.postValue(it.message) }
    }

}