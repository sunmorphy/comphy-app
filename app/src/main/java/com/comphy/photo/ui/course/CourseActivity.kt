package com.comphy.photo.ui.course

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.comphy.photo.databinding.ActivityCourseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCourseBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}