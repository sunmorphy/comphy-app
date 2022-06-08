package com.comphy.photo.ui.search.result.fragment.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ItemCommunitySearchBinding
import splitties.resources.drawable

class SearchCommunityAdapter(
    private val onClick: (content: FollowCommunityResponseContentItem) -> Unit
) : RecyclerView.Adapter<SearchCommunityAdapter.ViewHolder>() {

    private val communities = mutableListOf<FollowCommunityResponseContentItem>()

    fun setData(communities: List<FollowCommunityResponseContentItem>) {
        this.communities.clear()
        this.communities.addAll(communities)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchCommunityAdapter.ViewHolder {
        val view = ItemCommunitySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchCommunityAdapter.ViewHolder, position: Int) {
        val community = communities[position]
        with(holder.binding) {
            imgCommunityType.visibility = View.INVISIBLE
            Glide.with(holder.itemView)
                .load(community.profilePhotoCommunityLink)
                .placeholder(holder.itemView.drawable(R.drawable.ic_people_community))
                .error(holder.itemView.drawable(R.drawable.ic_people_community))
                .centerCrop()
                .into(imgCommunity)
            txtCommunityTitle.text = community.communityName
            txtCommunityCategory.text = community.categoryCommunity.name
        }

        holder.itemView.setOnClickListener { onClick(community) }
    }

    override fun getItemCount(): Int = communities.size

    inner class ViewHolder(var binding: ItemCommunitySearchBinding) : RecyclerView.ViewHolder(binding.root)
}