package com.comphy.photo.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemCommentSecondLevelBinding

class CommentSecondLevelAdapter(
    private val onClick: () -> Unit,
    private val onThirdLevelClick: () -> Unit
) : RecyclerView.Adapter<CommentSecondLevelAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentSecondLevelAdapter.ViewHolder {
        val view = ItemCommentSecondLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentSecondLevelAdapter.ViewHolder, position: Int) {
        var isReplyVisible = false
        holder.binding.apply {
            rvCommentThirdLevel.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                adapter = CommentThirdLevelAdapter(onThirdLevelClick)
            }
            layoutReply.setOnClickListener {
                if (isReplyVisible) {
                    rvCommentThirdLevel.visibility = View.GONE
                } else {
                    rvCommentThirdLevel.visibility = View.VISIBLE
                }
                isReplyVisible = !isReplyVisible
            }
        }
        holder.itemView.setOnClickListener { onClick() }
    }

    override fun getItemCount(): Int = 3

    inner class ViewHolder(var binding: ItemCommentSecondLevelBinding) :
        RecyclerView.ViewHolder(binding.root)
}