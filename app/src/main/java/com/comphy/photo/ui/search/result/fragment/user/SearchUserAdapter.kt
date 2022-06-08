package com.comphy.photo.ui.search.result.fragment.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.data.source.remote.response.user.following.UserFollowingResponseContentItem
import com.comphy.photo.databinding.ItemUserFollowBinding

class SearchUserAdapter(
    private val onFollow: (position: Int, userId: Int) -> Unit
) : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    val users = mutableListOf<UserResponseData>()

    fun setData(users: List<UserResponseData>) {
        this.users.clear()
        this.users.addAll(users)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchUserAdapter.ViewHolder {
        val view = ItemUserFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserAdapter.ViewHolder, position: Int) {
        val user = users[position]

        with(holder.binding) {
            if (user.profilePhotoLink != null) {
                Glide.with(holder.itemView)
                    .load(user.profilePhotoLink)
                    .error(R.drawable.ic_placeholder_people)
                    .placeholder(R.drawable.ic_placeholder_people)
                    .into(imgFollowProfile)
            }

            txtFollowUserName.text = user.fullname
            txtFollowUserJob.text = user.job

            when (user.isFollowed) {

            }
        }
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(var binding: ItemUserFollowBinding) : RecyclerView.ViewHolder(binding.root)
}