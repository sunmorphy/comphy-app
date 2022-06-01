package com.comphy.photo.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.user.following.UserFollowResponseContentItem
import com.comphy.photo.databinding.ItemUserFollowBinding

class FollowAdapter(
    private val follows: List<UserFollowResponseContentItem>,
//    private val isFollowing: Boolean,
//    private val onActionClick: () -> Unit
) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follow = follows[position]

        Glide.with(holder.itemView)
            .load(follow.userFollow.profilePhotoLink)
            .error(R.drawable.ic_placeholder_people)
            .placeholder(R.drawable.ic_placeholder_people)
            .into(holder.binding.imgFollowProfile)

        holder.binding.txtFollowUserName.text = follow.userFollow.fullname
        holder.binding.txtFollowUserJob.text = follow.userFollow.job

//        if (isFollowing) {
//            holder.binding.btnRemoveUser.apply {
//                visibility = View.VISIBLE
//                setOnClickListener {  }
//            }
//        }
    }

    override fun getItemCount(): Int = follows.size

    inner class ViewHolder(var binding: ItemUserFollowBinding) : RecyclerView.ViewHolder(binding.root)
}