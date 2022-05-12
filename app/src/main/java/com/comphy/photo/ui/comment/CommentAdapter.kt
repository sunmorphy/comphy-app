package com.comphy.photo.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemCommentSecondLevelBinding

class CommentAdapter(
    private val onClick: () -> Unit
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCommentSecondLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClick() }
    }

    override fun getItemCount(): Int = 9

    inner class ViewHolder(var binding: ItemCommentSecondLevelBinding) : RecyclerView.ViewHolder(binding.root)
}