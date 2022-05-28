package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContentItem
import com.comphy.photo.databinding.ItemPostTextBinding
import com.comphy.photo.vo.FollowType.FOLLOWED
import com.comphy.photo.vo.FollowType.NOT_FOLLOWED
import com.comphy.photo.vo.FollowType.OWNED
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.resources.str

internal class FeedTextViewHolder(
    var binding: ItemPostTextBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        view: View,
        content: FeedResponseContentItem,
//        onFollowClick: (postId: String, isFollowed: Int) -> Unit,
//        onBookmarkClick: (postId: String, isSaved: Boolean) -> Unit,
        onLikePressed: (postId: String, isLiked: Boolean) -> Unit
    ) {
        binding.btnAddUser.apply {
            when (content.isFollowed) {
                NOT_FOLLOWED -> {
                    setImageDrawable(drawable(R.drawable.ic_add_user))
//                    setOnClickListener { onFollowClick(content.id, content.isFollowed) }
                }
                FOLLOWED -> {
                    setImageDrawable(drawable(R.drawable.ic_remove_user))
//                    setOnClickListener { onFollowClick(content.id, content.isFollowed) }
                }
                OWNED -> visibility = View.INVISIBLE
            }
        }
        binding.btnBookmark.apply {
            imageTintList = if (content.postSaved) {
                colorSL(R.color.primary_orange)
            } else {
                colorSL(R.color.black)
            }
//            setOnClickListener { onBookmarkClick(content.id, content.postSaved) }
        }
        Glide.with(view)
            .load(content.userPost.profilePhotoLink)
            .centerCrop()
            .placeholder(view.drawable(R.drawable.ic_placeholder_people))
            .error(view.drawable(R.drawable.ic_placeholder_people))
            .into(binding.imgFeedProfile)
        binding.txtUserName.text = content.userPost.fullname
        binding.txtUserJob.text = content.userPost.job
        binding.txtFeedCaption.text = content.title
        binding.txtFeedDesc.text = content.description
        binding.btnLike.apply {
            if (content.liked) {
                backgroundTintList = view.colorSL(R.color.state_button_like_reverse)
                strokeColor = view.colorSL(R.color.state_button_like_reverse)
                text = view.str(R.string.feed_liked)
            } else {
                backgroundTintList = view.colorSL(R.color.state_button_like)
                strokeColor = view.colorSL(R.color.state_button_like)
                text = view.str(R.string.feed_like)
            }
            setOnClickListener { onLikePressed(content.id, content.liked) }
        }
    }
}