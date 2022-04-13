package com.comphy.photo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.comphy.photo.data.local.auth.AuthSharedPref
import com.comphy.photo.databinding.ActivityHomeBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AuthSharedPref.isLogin) {
            start<LoginActivity>()
            finishAffinity()
        }
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtAccessToken.text = AuthSharedPref.accessToken
        binding.txtRefreshToken.text = AuthSharedPref.refreshToken

        binding.btnSignOut.setOnClickListener {
            AuthSharedPref.clear()
            AuthSharedPref.isLogin = false
            start<HomeActivity>()
            finish()
        }
    }
}