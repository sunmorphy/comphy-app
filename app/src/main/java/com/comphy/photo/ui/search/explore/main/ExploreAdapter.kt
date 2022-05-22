package com.comphy.photo.ui.search.explore.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.data.model.ExploreTestModel
import com.comphy.photo.databinding.ItemExploreLandscapeBinding
import com.comphy.photo.databinding.ItemExplorePortraitBinding
import com.comphy.photo.databinding.ItemExploreSquareBinding
import com.comphy.photo.ui.search.explore.main.viewholder.ExploreLandscapeViewHolder
import com.comphy.photo.ui.search.explore.main.viewholder.ExplorePortraitViewHolder
import com.comphy.photo.ui.search.explore.main.viewholder.ExploreSquareViewHolder
import com.comphy.photo.vo.OrientationType

class ExploreAdapter(
    private val listMedia: List<ExploreTestModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        val media = listMedia[position]

        when (media.orientation) {
            OrientationType.SQUARE -> {
                (holder as ExploreSquareViewHolder).bind(holder.itemView, media.imageUrl)
            }
            OrientationType.PORTRAIT -> {
                (holder as ExplorePortraitViewHolder).bind(holder.itemView, media.imageUrl)
            }
            else -> {
                (holder as ExploreLandscapeViewHolder).bind(holder.itemView, media.imageUrl)
            }
        }
    }

    override fun getItemCount(): Int = listMedia.size

    override fun getItemViewType(position: Int): Int {
        val media = listMedia[position]

        return when (media.orientation) {
            OrientationType.SQUARE -> OrientationType.SQUARE
            OrientationType.PORTRAIT -> OrientationType.PORTRAIT
            else -> OrientationType.LANDSCAPE
        }
    }
}