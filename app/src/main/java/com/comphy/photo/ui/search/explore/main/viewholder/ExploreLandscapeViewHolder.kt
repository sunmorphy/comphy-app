package com.comphy.photo.ui.search.explore.main.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.ItemExploreLandscapeBinding
import splitties.resources.drawable

class ExploreLandscapeViewHolder(
    var binding: ItemExploreLandscapeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        view: View,
        imageUrl: String
    ) {
        Glide.with(view)
            .load(imageUrl)
            .placeholder(view.drawable(R.drawable.img_banner_placeholder))
            .error(view.drawable(R.drawable.img_banner_placeholder))
            .centerCrop()
            .into(binding.imgExplore)
    }

}