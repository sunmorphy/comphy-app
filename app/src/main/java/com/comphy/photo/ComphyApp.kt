package com.comphy.photo

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.comphy.photo.data.source.local.sharedpref.auth.AuthSharedPref
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
        setupHawk()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupHawk() {
        AuthSharedPref.appInit(this)
    }
}