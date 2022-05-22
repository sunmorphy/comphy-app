package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userAuth: UserAuth
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = userAuth.userRefreshToken
        val builder = response.request.newBuilder()

        return if (token != null) {
//            apiService.userRefresh(token)
            builder.header("Authorization", token).build()
        } else {
            null
        }
    }
}