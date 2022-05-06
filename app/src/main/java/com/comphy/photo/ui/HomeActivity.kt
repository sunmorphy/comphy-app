package com.comphy.photo.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.databinding.ActivityHomeBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var userAuth: UserAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!userAuth.isLogin) {
            start<LoginActivity>()
            finishAffinity()
        }
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtAccessToken.text = userAuth.userAccessToken
        binding.txtRefreshToken.text = userAuth.userRefreshToken

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
            userAuth.clear()
            userAuth.isLogin = false
//            AuthSharedPref.clear()
//            AuthSharedPref.isLogin = false
            start<HomeActivity>()
            finish()
        }
    }
}