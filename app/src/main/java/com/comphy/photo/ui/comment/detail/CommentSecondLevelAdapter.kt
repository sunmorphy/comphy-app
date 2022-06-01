package com.comphy.photo.ui.comment.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.comment.SecondChildComment
import com.comphy.photo.data.source.remote.response.post.comment.ThirdChildComment
import com.comphy.photo.databinding.ItemCommentSecondLevelBinding
import com.comphy.photo.utils.Extension.parseTimestamp
import splitties.resources.drawable
import splitties.resources.str

class CommentSecondLevelAdapter(
    private val onClick: (parentName: String, parentId: Int) -> Unit
) : PagingDataAdapter<SecondChildComment, CommentSecondLevelAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemCommentSecondLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)!!

        val thirdLevelComments = mutableListOf<ThirdChildComment>()
        if (thirdLevelComments.isNotEmpty()) thirdLevelComments.clear()
        thirdLevelComments.addAll(comment.thirdChildComment)

        var isThirdLevelReplyVisible = false

        with(holder.binding) {
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
                comment.thirdChildComment.size.toString()
            )
            rvCommentThirdLevel.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                adapter = CommentThirdLevelAdapter(thirdLevelComments)
            }
            layoutReply.setOnClickListener {
                if (isThirdLevelReplyVisible) {
                    rvCommentThirdLevel.visibility = View.GONE
                    isThirdLevelReplyVisible = !isThirdLevelReplyVisible
                } else {
                    if (!thirdLevelComments.isNullOrEmpty()) {
                        rvCommentThirdLevel.visibility = View.VISIBLE
                        isThirdLevelReplyVisible = !isThirdLevelReplyVisible
                    }
                }
            }
        }
        holder.itemView.setOnClickListener { onClick(comment.user.fullname, comment.id) }
    }

    inner class ViewHolder(var binding: ItemCommentSecondLevelBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SecondChildComment>() {
            override fun areItemsTheSame(
                oldItem: SecondChildComment,
                newItem: SecondChildComment
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SecondChildComment,
                newItem: SecondChildComment
            ): Boolean =
                oldItem == newItem
        }
    }
}