package com.comphy.photo.ui.register

import com.comphy.photo.base.BaseAuthViewModel
import com.comphy.photo.data.AuthRepository
import com.comphy.photo.data.local.auth.AuthSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    suspend fun userRegister(
        email: String,
        password: String,
        token: String,
        onComplete: () -> Unit
    ) {
        authRepository.userRegister(
            email,
            password,
            token,
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
