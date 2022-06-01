package com.comphy.photo.ui.community.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.member.MemberCommunityResponseContentItem
import com.comphy.photo.databinding.ItemCommunityMemberBinding

class CommunityMembersAdapter(
    private val members: List<MemberCommunityResponseContentItem>
) : RecyclerView.Adapter<CommunityMembersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityMembersAdapter.ViewHolder {
        val view = ItemCommunityMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityMembersAdapter.ViewHolder, position: Int) {
        val member = members[position]

        holder.binding.apply {
            txtMemberRole.visibility = View.INVISIBLE
            btnAdminAction.visibility = View.INVISIBLE
            btnBanMember.visibility = View.INVISIBLE
            txtMemberName.text = member.user.fullname
            txtMemberJob.text = member.user.job
            Glide.with(holder.itemView)
                .load(member.user.profilePhotoLink)
                .placeholder(R.drawable.ic_placeholder_people)
                .error(R.drawable.ic_placeholder_people)
                .into(imgMemberProfile)
        }
    }

    override fun getItemCount(): Int = members.size

    inner class ViewHolder(var binding: ItemCommunityMemberBinding) : RecyclerView.ViewHolder(binding.root)
}