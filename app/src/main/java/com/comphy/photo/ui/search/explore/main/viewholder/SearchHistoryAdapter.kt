package com.comphy.photo.ui.search.explore.main.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter(
    private val histories: List<String>,
    private val onClick: (key: String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchHistoryAdapter.ViewHolder {
        val view =
            ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchHistoryAdapter.ViewHolder, position: Int) {
        holder.binding.txtSearchHistory.text = histories[position]
        holder.itemView.setOnClickListener { onClick(histories[position]) }
    }

    override fun getItemCount(): Int = histories.size

    inner class ViewHolder(var binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}