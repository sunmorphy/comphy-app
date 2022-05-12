package com.comphy.photo.ui.main.fragment.feed.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemFeedImageLandscapeBinding
import com.comphy.photo.vo.FeedItemType

internal class FeedLandscapeViewHolder(
    var binding: ItemFeedImageLandscapeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: String,
        itemType: FeedItemType,
        onClick: (position: Int) -> Unit
    ) {}

}