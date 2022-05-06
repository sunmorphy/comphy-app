package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userAuth: UserAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = userAuth.userAccessToken
        val builder = chain.request().newBuilder()

        if (token != null) builder.addHeader("Authorization", "Bearer $token")

        return chain.proceed(builder.build())
    }
}