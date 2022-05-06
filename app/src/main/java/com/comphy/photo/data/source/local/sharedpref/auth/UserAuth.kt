package com.comphy.photo.data.source.local.sharedpref.auth

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class UserAuth(context: Context) : KotprefModel(context) {
    var userId by intPref(default = 0)
    var userAccessToken by nullableStringPref(default = null)
    var userRefreshToken by nullableStringPref(default = null)
    var isLogin by booleanPref(default = false)
}