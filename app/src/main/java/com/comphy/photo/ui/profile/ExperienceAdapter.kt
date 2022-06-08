package com.comphy.photo.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.data.source.remote.response.user.detail.Experience
import com.comphy.photo.databinding.ItemExperienceBinding

class ExperienceAdapter(
    private val experiences: List<Experience>,
    private val onClick: (experience: Experience, position: Int) -> Unit
) : RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExperienceAdapter.ViewHolder {
        val view = ItemExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceAdapter.ViewHolder, position: Int) {
        val experience = experiences[position]

        with(holder.binding) {
            txtExperienceTitle.text = experience.position
            txtExperienceDescription.text = experience.company
            txtExperienceDate.text = experience.years
        }

        holder.itemView.setOnClickListener { onClick(experience, position) }
    }

    override fun getItemCount(): Int = experiences.size

    inner class ViewHolder(var binding: ItemExperienceBinding) : RecyclerView.ViewHolder(binding.root)
}