package com.comphy.photo.ui.comment.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.comment.CommentResponseContentItem
import com.comphy.photo.databinding.ItemCommentSecondLevelBinding
import com.comphy.photo.utils.Extension.parseTimestamp
import splitties.resources.drawable
import splitties.resources.str

class CommentAdapter(
    private val onClick: (content: CommentResponseContentItem) -> Unit
) : PagingDataAdapter<CommentResponseContentItem, CommentAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCommentSecondLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)!!

        with(holder.binding) {
            rvCommentThirdLevel.visibility = View.GONE

            Glide.with(holder.itemView)
                .load(comment.user.profilePhotoLink)
                .centerCrop()
                .placeholder(holder.itemView.drawable(R.drawable.ic_placeholder_people))
                .error(holder.itemView.drawable(R.drawable.ic_placeholder_people))
                .into(imgProfile)

            txtCommentName.text = comment.user.fullname
            txtCommentJob.text = comment.user.job
            txtCommentTimePassed.text =
                comment.createdDate!!.parseTimestamp(holder.itemView.str(R.string.placeholder_time_passed))
            txtCommentContent.text = comment.comment
            txtReplyCount.text = String.format(
                holder.itemView.str(R.string.placeholder_comment_reply_count),
                comment.secondChildComment.size.toString()
            )
        }
        holder.itemView.setOnClickListener { onClick(comment) }
    }

    inner class ViewHolder(var binding: ItemCommentSecondLevelBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CommentResponseContentItem>() {
            override fun areItemsTheSame(
                oldItem: CommentResponseContentItem,
                newItem: CommentResponseContentItem
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CommentResponseContentItem,
                newItem: CommentResponseContentItem
            ): Boolean =
                oldItem == newItem
        }
    }
}