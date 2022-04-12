package com.comphy.photo.ui.login

import com.comphy.photo.base.BaseAuthViewModel
import com.comphy.photo.data.AuthRepository
import com.comphy.photo.data.local.auth.AuthSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseAuthViewModel() {

    suspend fun userLogin(email: String, password: String) {
        authRepository.userLogin(
            email,
            password,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                authResponse.postValue(it.toString())
                AuthSharedPref.accessToken = it.accessToken
                AuthSharedPref.refreshToken = it.refreshToken
                AuthSharedPref.isLogin = true
            }
    }

    suspend fun userLoginGoogle(email: String, token: String) {
        authRepository.userLoginGoogle(
            email,
            token,
            onError = { message.postValue(it.message) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                authResponse.postValue(it.toString())
                AuthSharedPref.accessToken = it.accessToken
                AuthSharedPref.refreshToken = it.refreshToken
                AuthSharedPref.isLogin = true
            }
    }

}