package com.comphy.photo.ui.main.fragment.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemFeedVideoBinding
import com.comphy.photo.vo.FeedItemType
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class FeedAdapter(
//    private val context: Context
//    private val posts: List<String>,
//    private val itemType: FeedItemType,
//    private val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private var player: ExoPlayer? = null

    inner class ViewHolder(var binding: ItemFeedVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
//        return when (viewType) {
//            PostType.TEXT -> FeedSquareViewHolder(
//                ItemFeedImageSquareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
//            PostType.MEDIA_SQUARE -> FeedSquareViewHolder(
//                ItemFeedImageSquareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
//            PostType.MEDIA_PORTRAIT -> FeedPortraitViewHolder(
//                ItemFeedImagePortraitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
//            PostType.MEDIA_LANDSCAPE -> FeedLandscapeViewHolder(
//                ItemFeedImageLandscapeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
//            else -> throw IllegalArgumentException("Missing view type exception ViewType: $viewType")
//        }
        val view = ItemFeedVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        player = ExoPlayer.Builder(holder.itemView.context)
            .build()
            .also { exoPlayer ->
                holder.binding.testVideoView.player = exoPlayer
                val mediaItem =
                    MediaItem.fromUri("https://res.cloudinary.com/dxrmgpqzm/video/upload/v1648384424/ttv8ak9d7ss4bzvosuyp.mp4")
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
//        return when (posts[position]) {
//            "square" -> (holder as FeedSquareViewHolder).bind(
//                "", itemType, onClick
//            )
//            "portrait" -> (holder as FeedPortraitViewHolder).bind(
//                "", itemType, onClick
//            )
//            "landscape" -> (holder as FeedLandscapeViewHolder).bind(
//                "", itemType, onClick
//            )
//            else -> {}
//        }
    }

    override fun getItemCount(): Int = 9

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        player?.stop()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        player?.stop()
    }
}