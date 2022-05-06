package com.comphy.photo.ui.auth.register

import com.comphy.photo.base.viewmodel.BaseAuthViewModel
import com.comphy.photo.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    suspend fun userRegister(name: String, email: String, password: String) {
        authRepository.userRegister(
            name,
            email,
            password,
            onError = { message.postValue(it.message) },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }

    suspend fun userRegisterGoogle(name: String, email: String, token: String) {
        authRepository.userRegisterGoogle(
            name,
            email,
            token,
            onError = { message.postValue(it.message) },
            onException = { responseException.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }
}