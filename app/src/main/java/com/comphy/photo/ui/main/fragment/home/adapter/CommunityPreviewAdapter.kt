package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.data.model.CommunityPreviewModel
import com.comphy.photo.databinding.ItemCommunityBinding
import com.comphy.photo.vo.CommunityType
import com.comphy.photo.vo.CommunityType.FOLLOWED
import com.comphy.photo.vo.CommunityType.OWN

class CommunityPreviewAdapter(
    private val communities: List<CommunityPreviewModel>?,
    private val communityType: CommunityType
) : RecyclerView.Adapter<CommunityPreviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityPreviewAdapter.ViewHolder {
        val view = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityPreviewAdapter.ViewHolder, position: Int) {
        if (communities != null) {
            val community = communities[position]

            when (communityType) {
                OWN -> {
                    holder.binding.txtCommunityTitle.text = community.communityTitle
                    holder.binding.txtCommunityCategory.text = community.communityCategory
                    Glide.with(holder.itemView).load(community.communityImage).centerCrop()
                        .into(holder.binding.imgCommunity)
                }
                FOLLOWED -> {
                    holder.binding.txtCommunityTitle.text = community.communityTitle
                    holder.binding.txtCommunityCategory.text = community.communityCategory
                    Glide.with(holder.itemView).load(community.communityImage).centerCrop()
                        .into(holder.binding.imgCommunity)
                }
            }
        }
    }

    override fun getItemCount(): Int = 2

    inner class ViewHolder(var binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root)
}