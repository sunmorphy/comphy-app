package com.comphy.photo.data.local.auth

import android.app.Application
import com.orhanobut.hawk.Hawk

object AuthSharedPref {
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val IS_LOGIN = "is_login"

    fun appInit(application: Application) {
        Hawk.init(application).build()
    }

    var accessToken: String? = null
        get() = Hawk.get(ACCESS_TOKEN)
        set(value) {
            Hawk.put(ACCESS_TOKEN, value)
            field = value
        }

    var refreshToken: String? = null
        get() = Hawk.get(REFRESH_TOKEN)
        set(value) {
            Hawk.put(REFRESH_TOKEN, value)
            field = value
        }

    var isLogin: Boolean = false
        get() = Hawk.get(IS_LOGIN, false)
        set(value) {
            Hawk.put(IS_LOGIN, value)
            field = value
        }

    fun clear() {
        Hawk.deleteAll()
    }
}