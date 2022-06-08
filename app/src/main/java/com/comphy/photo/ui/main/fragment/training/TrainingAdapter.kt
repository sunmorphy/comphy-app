package com.comphy.photo.ui.main.fragment.training

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.ItemTrainingMainBinding
import splitties.resources.drawable

class TrainingAdapter(
    private val events: List<EventResponseContentItem>,
    private val onBookmarkClick: (eventId: Int, isBookmarked: Boolean, position: Int) -> Unit,
    private val onClick: (event: EventResponseContentItem) -> Unit
) : RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingAdapter.ViewHolder {
        val view = ItemTrainingMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingAdapter.ViewHolder, position: Int) {
        val event = events[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(event.linkBanner)
                .placeholder(holder.itemView.drawable(R.drawable.img_banner_placeholder))
                .error(holder.itemView.drawable(R.drawable.img_banner_placeholder))
                .centerCrop()
                .into(imgTraining)

            txtEventTitle.text = event.eventName
            txtEventDescription.text = event.description
            txtEventDate // TODO: PARSING START DATE FROM BE
            btnBookmark.apply { onBookmarkClick(event.id, true, position) }
        }

        holder.itemView.setOnClickListener { onClick(event) }
    }

    override fun getItemCount(): Int = events.size

    inner class ViewHolder(var binding: ItemTrainingMainBinding) : RecyclerView.ViewHolder(binding.root)
}