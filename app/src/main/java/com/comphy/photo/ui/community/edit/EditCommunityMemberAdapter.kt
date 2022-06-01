package com.comphy.photo.ui.community.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemCommunityMemberBinding

class EditCommunityMemberAdapter(
    private val onBanClick: (Int) -> Unit
) : RecyclerView.Adapter<EditCommunityMemberAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditCommunityMemberAdapter.ViewHolder {
        val view = ItemCommunityMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditCommunityMemberAdapter.ViewHolder, position: Int) {
        // TODO: APPLY DATA FROM API
        holder.binding.btnBanMember.setOnClickListener {
            onBanClick(position) // TODO: THIS IS NOT ACTUALLY POSITION I DO THIS CUZ THE DATA WAS EMPTY
        }
    }

    override fun getItemCount(): Int = 9

    inner class ViewHolder(var binding: ItemCommunityMemberBinding) : RecyclerView.ViewHolder(binding.root)
}