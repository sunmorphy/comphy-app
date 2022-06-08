package com.comphy.photo.utils

import com.comphy.photo.data.source.remote.response.auth.AuthResponseData

object MockUtils {

    fun mockAuthResponseData() = AuthResponseData(
        "mockAccessToken",
        "mockRefreshToken",
        1
    )

}