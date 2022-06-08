package com.comphy.photo.ui.onboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comphy.photo.R
import com.comphy.photo.data.model.OnboardModel

class OnboardViewModel : ViewModel() {
    val listAssets = MutableLiveData(
        OnboardModel(
            listOf(R.drawable.img_onboard_1, R.drawable.img_onboard_2, R.drawable.img_onboard_3),
            listOf(R.string.title_onboard_1, R.string.title_onboard_2, R.string.title_onboard_3),
            listOf(R.string.desc_onboard_1, R.string.desc_onboard_2, R.string.desc_onboard_3)
        )
    )
}