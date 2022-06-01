package com.comphy.photo.ui.comment.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.comment.ThirdChildComment
import com.comphy.photo.databinding.ItemCommentThirdLevelBinding
import com.comphy.photo.utils.Extension.parseTimestamp
import splitties.resources.drawable
import splitties.resources.str

class CommentThirdLevelAdapter(
    private val comments: List<ThirdChildComment>
) : RecyclerView.Adapter<CommentThirdLevelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemCommentThirdLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]

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
        }
    }

    override fun getItemCount(): Int = comments.size

    inner class ViewHolder(var binding: ItemCommentThirdLevelBinding) :
        RecyclerView.ViewHolder(binding.root)
}