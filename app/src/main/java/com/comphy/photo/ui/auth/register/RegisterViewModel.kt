package com.comphy.photo.ui.auth.register

import com.comphy.photo.base.BaseAuthViewModel
import com.comphy.photo.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    suspend fun userRegister(email: String, password: String) {
        authRepository.userRegister(
            email,
            password,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }

    suspend fun userRegisterGoogle(email: String, token: String) {
        authRepository.userRegisterGoogle(
            email,
            token,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }
}