package com.comphy.photo.ui.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.ItemCommunityBinding
import splitties.resources.color

class CreatePostAdapter(
    private val communities: List<FollowCommunityResponseContentItem>?,
    private val onClick: (communityName: String, communityId: Int, categoryId: Int) -> Unit
) : RecyclerView.Adapter<CreatePostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.btnOption.visibility = View.INVISIBLE
        holder.binding.txtCommunityTitle.setTextColor(holder.itemView.color(R.color.state_button_text))
        holder.binding.txtCommunityCategory.setTextColor(holder.itemView.color(R.color.state_button_text))

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

            holder.itemView.setOnClickListener {
                onClick(
                    community.communityName,
                    community.id,
                    community.categoryCommunity.id
                )
            }
        }
    }

    override fun getItemCount(): Int = communities?.size ?: 2

    inner class ViewHolder(var binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root)
}