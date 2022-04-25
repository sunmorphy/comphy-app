package com.comphy.photo.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
    }

    protected abstract fun setupObserver()

}