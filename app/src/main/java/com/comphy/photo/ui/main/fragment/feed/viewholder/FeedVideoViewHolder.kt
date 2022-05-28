package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.databinding.ItemPostVideoBinding
import com.comphy.photo.vo.OrientationType
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import splitties.resources.colorSL

class FeedVideoViewHolder(
    var binding: ItemPostVideoBinding,
    private var player: ExoPlayer?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        view: View,
        content: FeedResponseContentItem,
        onLikePressed: (postId: String, isLiked: Boolean) -> Unit
    ) {
        when (content.orientationType) {
            OrientationType.SQUARE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.layoutFeedContent.id, "1:1")
                set.applyTo(binding.root)
            }
            OrientationType.LANDSCAPE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.layoutFeedContent.id, "16:9")
                set.applyTo(binding.root)
            }
            OrientationType.PORTRAIT -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.layoutFeedContent.id, "4:5")
                set.applyTo(binding.root)
            }
        }
        binding.txtFeedCaption.text = content.title
        binding.txtFeedDesc.text = content.description
        player = ExoPlayer.Builder(view.context)
            .build()
            .also {
                binding.playerFeedContent.player = it
                val mediaItem = content.linkVideo?.let { url -> MediaItem.fromUri(url) }
                if (mediaItem != null) {
                    it.setMediaItem(mediaItem)
                    it.prepare()
                }
            }
        binding.btnLike.apply {
            if (content.liked) {
                backgroundTintList = view.colorSL(R.color.state_button_like_reverse)
                strokeColor = view.colorSL(R.color.state_button_like_reverse)
            } else {
                backgroundTintList = view.colorSL(R.color.state_button_like)
                strokeColor = view.colorSL(R.color.state_button_like)
            }
            setOnClickListener { onLikePressed(content.id, content.liked) }
        }
    }

}