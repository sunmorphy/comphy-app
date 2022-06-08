package com.comphy.photo.ui.community.join

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ItemCommunitySearchBinding

class JoinCommunityAdapter(
    private val onClick: (community: FollowCommunityResponseContentItem) -> Unit
) : RecyclerView.Adapter<JoinCommunityAdapter.ViewHolder>() {

    private val communities = mutableListOf<FollowCommunityResponseContentItem>()

    fun setData(communities: List<FollowCommunityResponseContentItem>) {
        this.communities.clear()
        this.communities.addAll(communities)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JoinCommunityAdapter.ViewHolder {
        val view = ItemCommunitySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JoinCommunityAdapter.ViewHolder, position: Int) {
        val community = communities[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(community.profilePhotoCommunityLink)
                .placeholder(R.drawable.ic_people_community)
                .error(R.drawable.ic_people_community)
                .into(imgCommunity)
            txtCommunityTitle.text = community.communityName
            txtCommunityCategory.text = community.categoryCommunity.name
            if (community.communityType.lowercase().contains("pub")) {
                Glide.with(holder.itemView)
                    .load(R.drawable.ic_globe)
                    .into(imgCommunityType)
            } else {
                Glide.with(holder.itemView)
                    .load(R.drawable.ic_lock)
                    .into(imgCommunityType)
            }
        }
    }

    override fun getItemCount(): Int = communities.size

    inner class ViewHolder(var binding: ItemCommunitySearchBinding) : RecyclerView.ViewHolder(binding.root)
}