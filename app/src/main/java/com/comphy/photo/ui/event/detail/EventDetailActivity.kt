package com.comphy.photo.ui.event.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comphy.photo.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityEventDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}