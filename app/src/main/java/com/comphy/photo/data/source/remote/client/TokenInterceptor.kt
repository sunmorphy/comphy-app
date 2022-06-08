package com.comphy.photo.data.source.remote.client

import com.chibatching.kotpref.bulk
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.auth.AuthResponseData
import com.comphy.photo.utils.JsonParser.parseTo
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

class TokenInterceptor(
    private val userAuth: UserAuth,
    private val baseUrl: String,
    private val ioDispatcher: CoroutineDispatcher
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken: String? = userAuth.userAccessToken
        var request: Request = chain.request().newBuilder().build()

        if (accessToken != null) {
            request = newRequestWithAccessToken(chain.request(), accessToken)

            if (((userAuth.userLoggedTime - Date().time) / 60000) > 30) {
                println(userAuth.userLoggedTime)
                val apiService =
                    ApiClient(okHttpClient(userAuth, baseUrl, ioDispatcher)).instance(baseUrl)
                runBlocking(ioDispatcher) {
                    apiService.userRefresh(userAuth.userRefreshToken!!)
                        .suspendOnSuccess {
                            val parsedData = data.data?.parseTo(AuthResponseData::class.java)
                            userAuth.bulk {
                                userAccessToken = parsedData?.accessToken
                                userRefreshToken = parsedData?.refreshToken
                                userAuth.userLoggedTime = Date().time
                            }
                            val newAccessToken = userAuth.userAccessToken
                            request = newRequestWithAccessToken(chain.request(), newAccessToken!!)
                        }
                        .onError {
                            userAuth.bulk {
                                userAccessToken = null
                                userRefreshToken = null
                                userLoggedTime = -1
                                isLogin = false
                                isUserUpdated = false
                            }
                        }
                        .onException { println(message) }
                }
            }

//            println("TOKEN ${userAuth.userAccessToken}")
//            println("TOKEN ${userAuth.userRefreshToken}")
//            val response: Response = chain.proceed(request)
//            if (response.code === HttpURLConnection.HTTP_FORBIDDEN && response.body?.string()
//                    ?.parseTo(BaseResponse::class.java)?.message?.lowercase()?.trim()
//                    ?.contains("expired")!!
//            ) {
//            val apiService =
//                ApiClient(okHttpClient(userAuth, baseUrl, ioDispatcher)).instance(baseUrl)
////            CoroutineScope(ioDispatcher).launch {
//                    runBlocking {
////                    apiService.userRefresh(userAuth.userRefreshToken!!).data?.parseTo(
////                        AuthResponseData::class.java
////                    )?.accessToken!!
////                        val service =
//                apiService.userRefresh(userAuth.userRefreshToken!!).onSuccess {
//                    val parsedData = data.data?.parseTo(AuthResponseData::class.java)
//                    userAuth.userAccessToken = parsedData?.accessToken
//                    userAuth.userRefreshToken = parsedData?.refreshToken
//                    println("TOKEN AFTER ${userAuth.userAccessToken}")
//                    println("TOKEN AFTER ${userAuth.userRefreshToken}")
//                    val newAccessToken = userAuth.userAccessToken
//                    // Access token is refreshed in another thread.
//                    if (accessToken != newAccessToken && newAccessToken != "") {
//                        request = newRequestWithAccessToken(chain.request(), newAccessToken!!)
////                return chain.proceed(newRequestWithAccessToken(request, newAccessToken))
//                    }
//                }
//                    .onError {
//                        val errorResult: BaseMessageResponse? =
//                            errorBody?.string()
//                                ?.parseTo(BaseMessageResponse::class.java)
//                        println("REFRESH ERROR : ${errorResult?.message}")
//                        if (errorResult?.message?.lowercase()?.contains("expired")!!) {
//                            userAuth.blockingBulk {
//                                userAccessToken = null
//                                userRefreshToken = null
//                                isUserUpdated = false
//                            }
//                        }
//                    }
////                    }
//            }
//            return chain.proceed(request)
        }
        return chain.proceed(request)
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request().newBuilder()
//        var newRequest: Request
//        val token = userAuth.userAccessToken
//
//        if (token != null) request =
//            chain.request().newBuilder().addHeader("Authorization", "Bearer $token")
//
//        val requestBuild = request.build()
//
//        println(chain.proceed(requestBuild).code)
//        println(
//            chain.proceed(requestBuild).body?.string()?.parseTo(BaseResponse::class.java)?.message
//        )
//
//        if (chain.proceed(requestBuild).code == 403
//            && chain.proceed(requestBuild).body?.string()
//                ?.parseTo(BaseResponse::class.java)?.message?.lowercase()?.contains("expired")!!
//        ) {
//            request.removeHeader("Authorization")
//            runBlocking {
//                val apiService = ApiClient(okHttpClient(userAuth, baseUrl)).instance(baseUrl)
//                val apiResponse = apiService.userRefresh(userAuth.userRefreshToken!!)
//                apiResponse.suspendOnSuccess {
//                    val parsedData = data.data.parseTo(AuthResponseData::class.java)
//                    userAuth.userAccessToken = parsedData.accessToken
//                    userAuth.userRefreshToken = parsedData.refreshToken
//
//                    val newToken = userAuth.userAccessToken
//                    request.addHeader("Authorization", "Bearer $newToken")
//                }
//                    .onError { println(errorBody) }
//                    .onException { println(message) }
//            }
//        }
//        return chain.proceed(request.build())
//    }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request: Request = chain.request()
//        val builder = request.newBuilder()
//        val token: String? = userAuth.userAccessToken
//        if (token != null) {
//            setAuthHeader(builder, token) //write current token to request
//            request = builder.build() //overwrite old request
//            val response: Response =
//                chain.proceed(request) //perform request, here original request will be executed
//            if (response.code != 200 && response.body?.string()
//                    ?.parseTo(BaseResponse::class.java)?.message?.lowercase()?.trim()?.contains("expired")!!
//            ) { //if unauthorized
////            synchronized(apiService) {
//                //perform all 401 in sync blocks, to avoid multiply token updates
//
//                val currentToken = userAuth.userAccessToken
//                if (currentToken != null && currentToken == token) { //compare current token with token that was stored before, if it was not updated - do update
//                    val code = refreshToken() / 100 //refresh token
//                    if (code != 2) { //if refresh token failed for some reason
//                        if (code == 4) //only if response is 400, 500 might mean that token was not updated
//                            logout() //go to login screen
////                        return response //if token refresh failed - show error to user
//                    }
//                }
//                if (userAuth.userAccessToken != null) { //retry requires new auth token,
//                    setAuthHeader(builder, userAuth.userAccessToken) //set auth token to updated
//                    request = builder.build()
//                    return chain.proceed(request) //repeat request with new token
//                }
//
////            }
//            } else {
//                return response
//            }
//        } else {
//            return chain.proceed(builder.build())
//        }a
//    }

//    private fun setAuthHeader(builder: Request.Builder, token: String?) {
//        if (token != null) //Add Auth token to each request if authorized
//            builder.header("Authorization", "Bearer $token")
//    }
//
//    private fun refreshToken(): Int {
//        var code = -1
//        runBlocking {
//            val apiService = ApiClient(okHttpClient(userAuth, baseUrl)).instance(baseUrl)
//            apiService.userRefresh(userAuth.userRefreshToken!!).suspendOnSuccess {
//                val parsedData = data.data.parseTo(AuthResponseData::class.java)
//                userAuth.userAccessToken = parsedData.accessToken
//                userAuth.userRefreshToken = parsedData.refreshToken
//                code = statusCode.code
//            }
//        }
//        return code
//    }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        val currentToken = userAuth.userAccessToken
//        val originalRequest = chain.request()
//
//        return if (currentToken != null) {
//            if (chain.proceed(originalRequest).code == 403 && chain.proceed(originalRequest).body?.string()
//                    ?.parseTo(BaseResponse::class.java)?.message?.lowercase()?.trim()
//                    ?.contains("expired")!!
//            ) {
//                chain.proceedDeletingTokenOnError(
//                    chain.request().newBuilder().addHeaders(currentToken).build()
//                )
//            } else {
//                val refreshTokenEnp = "auth/refresh/token"
//                val refreshTokenRequest =
//                    originalRequest
//                        .newBuilder()
//                        .get()
//                        .url(baseUrl + refreshTokenEnp)
//                        .addHeaders(userAuth.userRefreshToken!!)
//                        .build()
//
//                val refreshResponse = chain.proceedDeletingTokenOnError(refreshTokenRequest)
//
//                if (refreshResponse.isSuccessful) {
//                    val refreshedToken = (refreshResponse.body?.string()?.parseTo(BaseResponse::class.java)
//                    ).transformToDomainEntity(
//                        currentToken.thirdParty
//                    )
//                    repository.saveTiendeoToken(refreshedToken)
//                    val newCall = originalRequest.newBuilder().addHeaders(refreshedToken).build()
//                    chain.proceedDeletingTokenOnError(newCall)
//
//                } else chain.proceedDeletingTokenOnError(chain.request())
//            }
//        } else {
//            return chain.proceed(originalRequest.newBuilder().build())
//        }
//
//    }
//
//    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
//        val response = proceed(request)
//        if (response.code == 403 && response.body?.string()
//                ?.parseTo(BaseResponse::class.java)?.message?.lowercase()?.trim()?.contains("expired")!!) {
//            userAuth.clear()
//        }
//        return response
//    }
//
//    private fun Request.Builder.addHeaders(token: String) =
//        this.apply { header("Authorization", "Bearer $token") }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = userAuth.userAccessToken
//        val request = chain.request()
//        val builder = request.newBuilder()
//
//        if (token != null) {
//            builder.addHeader("Authorization", "Bearer $token")
//
//            var response = chain.proceed(builder.build())
//            response.newBuilder()
//
//            if (response.code != 200 && response.body?.string()
//                    ?.parseTo(BaseResponse::class.java)?.message?.lowercase()?.trim()
//                    ?.contains("expired")!!
//            ) {
//                builder.removeHeader("Authorization")
//                response = chain.proceed(builder.build())
//                response.newBuilder()
//                val apiService = ApiClient(okHttpClient(userAuth, baseUrl))
//                runBlocking {
//                    val refreshResponse =
//                        apiService.instance(baseUrl).userRefresh(userAuth.userRefreshToken!!)
//                    refreshResponse.suspendOnSuccess {
//                        val parsedData = data.data?.parseTo(AuthResponseData::class.java)
//                        userAuth.userAccessToken = parsedData?.accessToken
//                        userAuth.userRefreshToken = parsedData?.refreshToken
//                    }
//                        .onError { userAuth.clear() }
//                }
//                builder.addHeader("Authorization", "Bearer ${userAuth.userAccessToken}")
//            }
//        }
//        return chain.proceed(builder.build())
//    }

    //    private fun logout() {
//        userAuth.userAccessToken = null
//        userAuth.userRefreshToken = null
//        userAuth.isUserUpdated = false
//        userAuth.isLogin = false
//    }
//    override fun intercept(chain: Interceptor.Chain): Response {
//
//        val originalRequest = chain.request()
//        val authenticationRequest = originalRequest.newBuilder()
//            .header("Authorization", "fail")
//            .build()
//
//        val origResponse = chain.proceed(authenticationRequest)
//
//        // server should give us a 403, since the header contains 'fail'
//
//        // server should give us a 403, since the header contains 'fail'
//        return if (origResponse.code === 403) {
//            val refreshToken = "abcd" // you got this from Auth0 when logging in
//
//            // start a new synchronous network call to Auth0
//            val newIdToken: String = fetchNewIdTokenFromAuth0(refreshToken)
//
//            // make a new request with the new id token
//            val newAuthenticationRequest = originalRequest.newBuilder()
//                .header("TestHeader", "succeed")
//                .build()
//
//            // try again
//
//            // hopefully we now have a status of 200
//            chain.proceed(newAuthenticationRequest)
//        } else {
//            origResponse
//        }
//    }
}