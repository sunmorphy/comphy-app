package com.comphy.photo.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
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

        val fruits = listOf(
            "Strawberry\npie",
            "Apple\npie",
            "Orange\njuice",
            "Lemon\njuice",
            "Beer",
            "Lime",
            "Watermelon",
            "Blueberry",
            "Plum"
        )

        val fruitsAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                fruits
            )

//        binding.edtAutocomplete.doAfterTextChanged {
            binding.edtAutocomplete.apply {
                setAdapter(fruitsAdapter)
                threshold = 1
            }
//        }

        binding.btnSignOut.setOnClickListener {
            AuthSharedPref.clear()
            AuthSharedPref.isLogin = false
            start<HomeActivity>()
            finish()
        }
    }
}