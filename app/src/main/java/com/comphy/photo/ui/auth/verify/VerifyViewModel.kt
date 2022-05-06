package com.comphy.photo.ui.auth.verify

import androidx.lifecycle.MutableLiveData
import com.comphy.photo.base.viewmodel.BaseAuthViewModel
import com.comphy.photo.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    val resendMessage = MutableLiveData<String>()

    suspend fun userRegisterVerify(otp: String, email: String) {
        authRepository.userRegisterVerify(
            otp,
            email,
            onError = { message.postValue(it.message) },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }

    suspend fun userRegisterResendCode(name: String, email: String, password: String) {
        authRepository.userRegister(
            name,
            email,
            password,
            onError = { resendMessage.postValue("Failed Resend Code") },
            onException = { message.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { resendMessage.postValue("Resend Code Success") }
    }

    suspend fun userRegisterGoogleResendCode(name: String, email: String, token: String) {
        authRepository.userRegisterGoogle(
            name,
            email,
            token,
            onError = { message.postValue(it.message) },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { resendMessage.postValue("Resend Code Success") }
    }

    suspend fun userForgotVerify(otp: String, email: String) {
        authRepository.userForgotVerify(
            otp,
            email,
            onError = { message.postValue(it.message) },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }

    suspend fun userForgotResendCode(email: String) {
        authRepository.userForgot(
            email,
            onError = { resendMessage.postValue("Failed Resend Code") },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { resendMessage.postValue("Resend Code Success") }
    }

}