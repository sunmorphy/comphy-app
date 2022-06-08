package com.comphy.photo.ui.auth.login

import com.comphy.photo.base.viewmodel.BaseAuthViewModel
import com.comphy.photo.data.repository.AuthRepository
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.auth.AuthBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userAuth: UserAuth
) : BaseAuthViewModel() {

    suspend fun userLogin(email: String, password: String) {
        val authBody = AuthBody(username = email, password = password)
        authRepository.userLogin(
            authBody,
            onError = { message.postValue(it?.message) },
            onException = { exceptionResponse.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                authResponse.postValue(it.toString())
                userAuth.userId = it.userId!!
                userAuth.userAccessToken = it.accessToken
                userAuth.userRefreshToken = it.refreshToken
                userAuth.userLoggedTime = Date().time
                userAuth.isLogin = true
            }
    }

    suspend fun userLoginGoogle(email: String, token: String) {
        authRepository.userLoginGoogle(
            email,
            token,
            onError = { message.postValue(it?.message) },
            onException = { exceptionResponse.postValue(it) }
        )
            .onStart { isLoading.postValue(true) }
            .onCompletion { isLoading.postValue(false) }
            .collect {
                authResponse.postValue(it.toString())
                userAuth.userId = it.userId!!
                userAuth.userAccessToken = it.accessToken
                userAuth.userRefreshToken = it.refreshToken
                userAuth.isLogin = true
            }
    }

}