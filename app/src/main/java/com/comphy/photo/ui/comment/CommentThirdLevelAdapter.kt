package com.comphy.photo.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemCommentThirdLevelBinding

class CommentThirdLevelAdapter(
    private val onClick: () -> Unit
) : RecyclerView.Adapter<CommentThirdLevelAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemCommentThirdLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClick() }
    }

    override fun getItemCount(): Int = 2

    inner class ViewHolder(var binding: ItemCommentThirdLevelBinding) :
        RecyclerView.ViewHolder(binding.root)
}