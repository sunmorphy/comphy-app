package com.comphy.photo.ui.course.subject.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.comphy.photo.databinding.ActivityCourseSubjectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseSubjectDetailActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCourseSubjectBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}