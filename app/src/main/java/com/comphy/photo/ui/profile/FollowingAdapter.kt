package com.comphy.photo.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.user.following.UserFollowingResponseContentItem
import com.comphy.photo.databinding.ItemUserFollowBinding
import com.comphy.photo.vo.FollowType

class FollowingAdapter(
    private val follows: List<UserFollowingResponseContentItem>,
    private val onActionClick: (position: Int, userId: Int, isFollowed: Boolean) -> Unit
) : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {
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

        when (follow.userFollow.isFollowed) {
            FollowType.FOLLOWED -> {
                holder.binding.btnAddUser.visibility = View.INVISIBLE
                holder.binding.btnRemoveUser.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        onActionClick(
                            position,
                            follow.userFollow.id,
                            (follow.userFollow.isFollowed == FollowType.FOLLOWED)
                        )
                        follow.userFollow.isFollowed = FollowType.NOT_FOLLOWED
                    }
                }
            }
            FollowType.NOT_FOLLOWED -> {
                holder.binding.btnRemoveUser.visibility = View.INVISIBLE
                holder.binding.btnAddUser.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        onActionClick(
                            position,
                            follow.userFollow.id,
                            (follow.userFollow.isFollowed == FollowType.FOLLOWED)
                        )
                        follow.userFollow.isFollowed = FollowType.FOLLOWED
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = follows.size

    inner class ViewHolder(var binding: ItemUserFollowBinding) : RecyclerView.ViewHolder(binding.root)
}