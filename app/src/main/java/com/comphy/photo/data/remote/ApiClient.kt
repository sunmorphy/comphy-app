package com.comphy.photo.data.remote

import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun instance(baseUrl: String): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(logging).build())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    private val logging: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
}