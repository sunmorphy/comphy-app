package com.comphy.photo.ui.community.join

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemCommunitySearchBinding

class JoinCommunityAdapter : RecyclerView.Adapter<JoinCommunityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JoinCommunityAdapter.ViewHolder {
        val view = ItemCommunitySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JoinCommunityAdapter.ViewHolder, position: Int) {
        // TODO: APPLY DATA FROM API
    }

    override fun getItemCount(): Int = 9

    inner class ViewHolder(var binding: ItemCommunitySearchBinding) : RecyclerView.ViewHolder(binding.root)
}