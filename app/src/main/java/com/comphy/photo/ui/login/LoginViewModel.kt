package com.comphy.photo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comphy.photo.data.AuthRepository
import com.comphy.photo.data.local.auth.AuthSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    suspend fun userLogin(username: String, password: String, onComplete: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            authRepository.userLogin(username, password)
                .onStart { isLoading.postValue(true) }
                .onCompletion {
                    isLoading.postValue(false)
                    onComplete()
                }
                .collect { AuthSharedPref.accessToken = it.accessToken }
        }
    }

    suspend fun userGoogleLogin(email: String, token: String, onComplete: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            authRepository.userGoogleLogin(email, token)
                .onStart {
                    Timber.tag("ON START").w("GOOGLE LOGIN")
                    isLoading.postValue(true)
                }
                .onCompletion {
                    isLoading.postValue(false)
                    onComplete()
                }
                .collect {
                    Timber.tag("USER ACCESS TOKEN").e(it.accessToken)
                    AuthSharedPref.accessToken = it.accessToken
                    AuthSharedPref.refreshToken = it.refreshToken
                }
        }
    }

}