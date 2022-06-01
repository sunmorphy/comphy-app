package com.comphy.photo.ui.search.explore.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.databinding.ItemExploreLandscapeBinding
import com.comphy.photo.databinding.ItemExplorePortraitBinding
import com.comphy.photo.databinding.ItemExploreSquareBinding
import com.comphy.photo.ui.search.explore.main.viewholder.ExploreLandscapeViewHolder
import com.comphy.photo.ui.search.explore.main.viewholder.ExplorePortraitViewHolder
import com.comphy.photo.ui.search.explore.main.viewholder.ExploreSquareViewHolder
import com.comphy.photo.vo.OrientationType

class ExploreAdapter(
    private val onClick: (contentItem: FeedResponseContentItem) -> Unit
) : PagingDataAdapter<FeedResponseContentItem, RecyclerView.ViewHolder>(COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            OrientationType.SQUARE -> ExploreSquareViewHolder(
                ItemExploreSquareBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OrientationType.PORTRAIT -> ExplorePortraitViewHolder(
                ItemExplorePortraitBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ExploreLandscapeViewHolder(
                ItemExploreLandscapeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val media = getItem(position)!!

        when (media.orientationType) {
            OrientationType.SQUARE -> {
                (holder as ExploreSquareViewHolder).bind(holder.itemView, media.linkPhoto ?: "")
            }
            OrientationType.PORTRAIT -> {
                (holder as ExplorePortraitViewHolder).bind(holder.itemView, media.linkPhoto ?: "")
            }
            else -> {
                (holder as ExploreLandscapeViewHolder).bind(holder.itemView, media.linkPhoto ?: "")
            }
        }

        holder.itemView.setOnClickListener { onClick(media) }
    }

    override fun getItemViewType(position: Int): Int {
        val media = getItem(position)!!

        return when (media.orientationType) {
            OrientationType.SQUARE -> OrientationType.SQUARE
            OrientationType.PORTRAIT -> OrientationType.PORTRAIT
            else -> OrientationType.LANDSCAPE
        }
    }

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
                oldItem.id == newItem.id
        }
    }
}