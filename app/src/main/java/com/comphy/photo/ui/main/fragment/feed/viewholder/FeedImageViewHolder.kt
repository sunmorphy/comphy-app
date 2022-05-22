package com.comphy.photo.ui.main.fragment.feed.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.post.feed.FeedResponseContent
import com.comphy.photo.databinding.ItemFeedImageLandscapeBinding
import com.comphy.photo.vo.OrientationType
import splitties.resources.colorSL
import splitties.resources.drawable

class FeedImageViewHolder(
    var binding: ItemFeedImageLandscapeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        view: View,
        content: FeedResponseContent,
        onLikePressed: (postId: String, isLiked: Boolean) -> Unit
    ) {
        when (content.orientationType) {
            OrientationType.SQUARE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.imgFeedContent.id, "1:1")
                set.applyTo(binding.root)
            }
            OrientationType.LANDSCAPE -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.imgFeedContent.id, "16:9")
                set.applyTo(binding.root)
            }
            OrientationType.PORTRAIT -> {
                val set = ConstraintSet()
                set.clone(binding.root)
                set.setDimensionRatio(binding.imgFeedContent.id, "4:5")
                set.applyTo(binding.root)
            }
        }
        Glide.with(view)
            .load(content.userPost.profilePhotoLink)
            .centerCrop()
            .placeholder(view.drawable(R.drawable.ic_placeholder_people))
            .error(view.drawable(R.drawable.ic_placeholder_people))
            .into(binding.imgFeedProfile)

        Glide.with(view)
            .load(content.linkPhoto)
            .centerCrop()
            .into(binding.imgFeedContent)

        binding.txtUserName.text = content.userPost.fullname
        binding.txtUserJob.text = content.userPost.job
        binding.txtFeedCaption.text = content.title
        binding.txtDataExifCamera.text = String.format(view.resources.getString(R.string.placeholder_camera), content.camera ?: "-")
        binding.txtDataExifLens.text = String.format(view.resources.getString(R.string.placeholder_lens), content.lens ?: "-")
        binding.txtDataExifFlash.text = String.format(view.resources.getString(R.string.placeholder_flash), content.flash ?: "-")
        binding.txtDataExifIso.text = String.format(view.resources.getString(R.string.placeholder_iso), content.iso ?: "-")
        binding.txtDataExifShutter.text = String.format(view.resources.getString(R.string.placeholder_shutter), content.shutterSpeed ?: "-")
        binding.txtDataExifAperture.text = String.format(view.resources.getString(R.string.placeholder_aperture), content.aperture ?: "-")
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