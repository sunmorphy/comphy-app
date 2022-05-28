package com.comphy.photo.data.source.remote.client

import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.auth.AuthResponseData
import com.comphy.photo.utils.JsonParser.parseTo
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

//class TokenAuthenticator(
//    private val userAuth: UserAuth,
//    private val baseUrl: String
//) : Authenticator {
//    override fun authenticate(route: Route?, response: Response): Request? {
//        return runBlocking {
//            if (isTokenRefreshed()) {
//                response.request.newBuilder()
//                    .header("Authorization", "Bearer ${userAuth.userAccessToken!!}")
//                    .build()
//            } else {
//                null
//            }
//        }
//    }
//
//    private suspend fun isTokenRefreshed(): Boolean {
//        val apiService = ApiClient(okHttpClient(userAuth, baseUrl)).instance(baseUrl)
//        val apiResponse = apiService.userRefresh(userAuth.userRefreshToken!!)
//        var responseCode = -1
//
//        apiResponse.suspendOnSuccess {
//            responseCode = statusCode.code
//            val parsedData = data.data?.parseTo(AuthResponseData::class.java)
//            userAuth.userAccessToken = parsedData?.accessToken
//            userAuth.userRefreshToken = parsedData?.refreshToken
//        }
//        return responseCode == 200
//    }
//}

//class TokenAuthenticator(
//    private val userAuth: UserAuth,
//    private val baseUrl: String
//) : Authenticator {
//    override fun authenticate(route: Route?, response: Response): Request? {
//        return runBlocking {
//            userAuth.userRefreshToken?.let {
//                val apiService = ApiClient(okHttpClient(userAuth, baseUrl)).instance(baseUrl)
//                apiService.userRefresh(it).suspendOnSuccess {
//                    userAuth.userAccessToken = data.accessToken
//                    userAuth.userRefreshToken = data.refreshToken
//                }
//                val builder = response.request.newBuilder()
//                builder.header("Authorization", "Bearer ${userAuth.userAccessToken!!}").build()
//            }
//        }
//    }
//}