package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContent
import com.comphy.photo.databinding.ItemFeedTextBinding
import com.google.android.material.button.MaterialButton
import splitties.resources.colorSL

internal class FeedTextViewHolder(
    var binding: ItemFeedTextBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        view: View,
        content: FeedResponseContent,
        onLikePressed: (postId: String, isLiked: Boolean) -> Unit
    ) {
        binding.txtFeedCaption.text = content.title
        binding.txtFeedDesc.text = content.description
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