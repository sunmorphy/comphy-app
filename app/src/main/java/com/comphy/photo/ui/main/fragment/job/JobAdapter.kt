package com.comphy.photo.ui.main.fragment.job

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemJobBinding

class JobAdapter(
    private val jobs: List<Any>,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.ViewHolder {
        val view = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClick() }
    }

    override fun getItemCount(): Int = jobs.size

    inner class ViewHolder(var binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root)

}