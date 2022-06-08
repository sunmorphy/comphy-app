package com.comphy.photo.ui.community.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.member.MemberCommunityResponseContentItem
import com.comphy.photo.databinding.ItemCommunityMemberBinding

class EditCommunityMemberAdapter(
    private val members: List<MemberCommunityResponseContentItem>,
    private val onBanClick: (userId: Int, username: String) -> Unit
) : RecyclerView.Adapter<EditCommunityMemberAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditCommunityMemberAdapter.ViewHolder {
        val view =
            ItemCommunityMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditCommunityMemberAdapter.ViewHolder, position: Int) {
        val member = members[position]

        holder.binding.apply {
            btnAdminAction.visibility = View.INVISIBLE
            btnBanMember.apply {
                visibility = View.VISIBLE
                setOnClickListener { onBanClick(member.user.id, member.user.fullname) }
            }
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

    inner class ViewHolder(var binding: ItemCommunityMemberBinding) :
        RecyclerView.ViewHolder(binding.root)
}