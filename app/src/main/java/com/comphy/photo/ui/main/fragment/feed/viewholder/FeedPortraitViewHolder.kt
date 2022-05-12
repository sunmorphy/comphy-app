package com.comphy.photo.ui.main.fragment.feed.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemFeedImagePortraitBinding
import com.comphy.photo.vo.FeedItemType

internal class FeedPortraitViewHolder(
    var binding: ItemFeedImagePortraitBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: String,
        itemType: FeedItemType,
        onClick: (position: Int) -> Unit
    ) {}

}