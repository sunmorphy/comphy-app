package com.comphy.photo.ui.event.all

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comphy.photo.databinding.ActivityAllEventBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllEventActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAllEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}