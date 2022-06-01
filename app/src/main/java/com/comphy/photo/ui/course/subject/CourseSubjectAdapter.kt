package com.comphy.photo.ui.course.subject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemCourseChapterBinding

class CourseSubjectAdapter : RecyclerView.Adapter<CourseSubjectAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseSubjectAdapter.ViewHolder {
        val view = ItemCourseChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseSubjectAdapter.ViewHolder, position: Int) {
        // TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = 9

    inner class ViewHolder(var binding: ItemCourseChapterBinding) : RecyclerView.ViewHolder(binding.root)
}