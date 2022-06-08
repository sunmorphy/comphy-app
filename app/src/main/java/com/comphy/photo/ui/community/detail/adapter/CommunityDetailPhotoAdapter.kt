package com.comphy.photo.ui.community.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.databinding.ItemCommunityImageBinding
import com.comphy.photo.vo.FollowType.FOLLOWED
import com.comphy.photo.vo.FollowType.NOT_FOLLOWED
import com.comphy.photo.vo.FollowType.OWNED
import com.comphy.photo.vo.OrientationType
import splitties.resources.drawable

class CommunityDetailPhotoAdapter(
    private val onProfileClick: (userId: Int) -> Unit,
    private val onFollowClick: (userId: Int, isFollowed: Int) -> Unit
) : PagingDataAdapter<FeedResponseContentItem, CommunityDetailPhotoAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemCommunityImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)!!
        holder.binding.apply {
            btnAddUser.apply {
                when (post.isFollowed) {
                    NOT_FOLLOWED -> {
                        setImageDrawable(drawable(R.drawable.ic_add_user))
                        setOnClickListener { onFollowClick(post.userPost.id, post.isFollowed) }
                    }
                    FOLLOWED -> {
                        setImageDrawable(drawable(R.drawable.ic_remove_user))
                        setOnClickListener { onFollowClick(post.userPost.id, post.isFollowed) }
                    }
                    OWNED -> visibility = View.INVISIBLE
                }
            }
            when (post.orientationType) {
                OrientationType.SQUARE -> {
                    val set = ConstraintSet()
                    set.clone(root)
                    set.setDimensionRatio(imgContent.id, "1:1")
                    set.applyTo(root)
                }
                OrientationType.LANDSCAPE -> {
                    val set = ConstraintSet()
                    set.clone(root)
                    set.setDimensionRatio(imgContent.id, "16:9")
                    set.applyTo(root)
                }
                OrientationType.PORTRAIT -> {
                    val set = ConstraintSet()
                    set.clone(root)
                    set.setDimensionRatio(imgContent.id, "4:5")
                    set.applyTo(root)
                }
            }
            Glide.with(holder.itemView)
                .load(post.userPost.profilePhotoLink)
                .centerCrop()
                .placeholder(holder.itemView.drawable(R.drawable.ic_placeholder_people))
                .error(holder.itemView.drawable(R.drawable.ic_placeholder_people))
                .into(imgProfile)

            Glide.with(holder.itemView)
                .load(post.linkPhoto)
                .centerCrop()
                .into(imgContent)

            txtUserName.text = post.userPost.fullname
            txtUserJob.text = post.userPost.job
            imgProfile.setOnClickListener { onProfileClick(post.userPost.id) }
        }
    }

    inner class ViewHolder(var binding: ItemCommunityImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<FeedResponseContentItem>() {
            override fun areItemsTheSame(
                oldItem: FeedResponseContentItem,
                newItem: FeedResponseContentItem
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FeedResponseContentItem,
                newItem: FeedResponseContentItem
            ): Boolean =
                oldItem == newItem
        }
    }
}