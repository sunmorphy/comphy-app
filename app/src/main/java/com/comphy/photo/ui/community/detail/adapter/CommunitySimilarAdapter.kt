package com.comphy.photo.ui.community.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ItemCommunityBinding

class CommunitySimilarAdapter(
    private val onItemClick: (FollowCommunityResponseContentItem) -> Unit
) : RecyclerView.Adapter<CommunitySimilarAdapter.ViewHolder>() {

    private val communities = mutableListOf<FollowCommunityResponseContentItem>()

    fun setData(communities: List<FollowCommunityResponseContentItem>) {
        this.communities.clear()
        this.communities.addAll(communities)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunitySimilarAdapter.ViewHolder {
        val view = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunitySimilarAdapter.ViewHolder, position: Int) {
        val community = communities[position]

        holder.binding.btnOption.visibility = View.INVISIBLE
        Glide.with(holder.itemView)
            .load(community.profilePhotoCommunityLink)
            .error(R.drawable.ic_people_community)
            .placeholder(R.drawable.ic_people_community)
            .centerCrop()
            .into(holder.binding.imgCommunity)
        holder.binding.txtCommunityTitle.text = community.communityName
        holder.binding.txtCommunityCategory.text = community.categoryCommunity.name
        holder.itemView.setOnClickListener { onItemClick(community) }
    }

    override fun getItemCount(): Int = if (communities.size > 2) 2 else communities.size

    inner class ViewHolder(var binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root)
}