package com.comphy.photo.ui.auth.forgot

import com.comphy.photo.base.BaseAuthViewModel
import com.comphy.photo.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    suspend fun userForgot(email: String) {
        authRepository.userForgot(
            email,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect { authResponse.postValue(it.message) }
    }
}