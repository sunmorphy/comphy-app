package com.comphy.photo.ui.community.all

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.created.CreatedCommunityResponseContent
import com.comphy.photo.databinding.ItemCommunityBinding

class AllCommunityAdapter(
    private val communities: List<CreatedCommunityResponseContent>?,
    private val onOptionClick: (Int) -> Unit
) : RecyclerView.Adapter<AllCommunityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllCommunityAdapter.ViewHolder {
        val view = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllCommunityAdapter.ViewHolder, position: Int) {
        if (communities != null) {
            val community = communities[position]

            Glide.with(holder.itemView)
                .load(community.profilePhotoCommunityLink)
                .error(R.drawable.ic_people_community)
                .placeholder(R.drawable.ic_people_community)
                .centerCrop()
                .into(holder.binding.imgCommunity)
            holder.binding.txtCommunityTitle.text = community.communityName
            holder.binding.txtCommunityCategory.text = community.categoryCommunity.name
            holder.binding.btnOption.setOnClickListener { onOptionClick(community.id) }
        }
    }

    override fun getItemCount(): Int = communities?.size ?: 2

    inner class ViewHolder(var binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root)
}