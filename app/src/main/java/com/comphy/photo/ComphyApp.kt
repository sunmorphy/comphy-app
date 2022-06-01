package com.comphy.photo

import android.app.Application
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ComphyApp : Application() {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun baseUrl(): String

    external fun clientId(): String

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}