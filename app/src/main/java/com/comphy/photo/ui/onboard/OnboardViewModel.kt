package com.comphy.photo.ui.onboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.R
import com.comphy.photo.data.model.OnboardModel
import com.comphy.photo.data.repository.UserRepository
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.auth.AuthResponseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userAuth: UserAuth
) : ViewModel() {

    val userData = MutableLiveData<AuthResponseUser>()

//    suspend fun getUserDetails() {
//        if (userAuth.userId != 0) {
//            userRepository.getUserDetails()
////                .onStart { isFetching.postValue(true) }
////                .onCompletion { isFetching.postValue(false) }
//                .collect {
//                    if (it.responseData != null) {
//                        userData.postValue(it.responseData!!.user!!)
//                    }
//                }
//        }
//    }

    val listAssets = MutableLiveData(
        OnboardModel(
            listOf(R.drawable.img_onboard_1, R.drawable.img_onboard_2, R.drawable.img_onboard_3),
            listOf(R.string.title_onboard_1, R.string.title_onboard_2, R.string.title_onboard_3),
            listOf(R.string.desc_onboard_1, R.string.desc_onboard_2, R.string.desc_onboard_3)
        )
    )
}