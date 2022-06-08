package com.comphy.photo.ui.job

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemJobDescBinding

class JobDescAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<JobDescAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobDescAdapter.ViewHolder {
        val view = ItemJobDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobDescAdapter.ViewHolder, position: Int) {
        val desc = "${position + 1} ${items[position]}"
        holder.binding.txtJobDescriptionContent.text = desc
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(var binding: ItemJobDescBinding): RecyclerView.ViewHolder(binding.root)
}